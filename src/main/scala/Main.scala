
import io.circe.syntax.EncoderOps
import org.jsoup.Jsoup
import org.jsoup.parser.StreamParser
import zio.ZIOAppDefault
import zio.http._
import zio.ZIO

object Main extends ZIOAppDefault {
  def run = Server.serve(routes).provide(Server.default)

  val routes = Routes(
    Method.POST / "title" -> handler { (req: Request) =>
      getTitles(req)
    }
  )
    .handleError(e => Response.internalServerError(e.getMessage))

  private def getTitles(req: Request) = {
    for {
      body <- req.body.asString
      _ <- ZIO.logTrace(s"body = $body")
      urls <- io.circe.parser.parse(body).flatMap(_.as[Set[String]]) match {
        case Left(failure) =>
          ZIO.logError(failure.getMessage) *>
            ZIO.fail(new IllegalArgumentException(s"Fail to parse urls list: ${failure.getMessage}"))
        case Right(list) =>
          ZIO.succeed(list)
      }
      _ <- ZIO.logTrace(s"urls = $urls")
      urlsWithTitles <- ZIO.foreachPar(urls) { url =>
        ZIO.acquireReleaseWith(acquire(url))(release)(use(url))
      }
      _ <- ZIO.logTrace(s"urlsWithTitles = $urlsWithTitles")
    } yield Response.text(urlsWithTitles.toMap.asJson.noSpaces)
  }

  private def use(url: String)(parser: StreamParser) = ZIO.attempt {
    val title = Option(parser.selectFirst("title")).map(_.text).getOrElse("")
    (url -> title)
  }.catchAll { ex: Throwable =>
    ZIO.logError(s"Fail to get title of '$url': ${ex.getMessage}") *>
      ZIO.succeed((url -> ""))
  }

  private def release(parser: StreamParser) = ZIO.succeed(parser.close())

  private def acquire(url: String) = ZIO.attempt(Jsoup.connect(url).execute().streamParser())
}