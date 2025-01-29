import zio.ZIO
import zio.test._
import zio.http._

object RouteSpec extends ZIOSpecDefault {

  def spec = suite("http")(
    test("get titles") {
      for {
        client <- ZIO.service[Client]
        _ <- TestClient.addRoutes(Main.routes)
        request = Request.post(URL.root / "title", Body.fromString("""["https://zio.dev","https://github.com"]"""))
        response <- client.request(request)
        responseBody <- response.body.asString
      } yield assertTrue(responseBody == """{"https://zio.dev":"ZIO | ZIO","https://github.com":"GitHub · Build and ship software on a single, collaborative platform · GitHub"}""")
    }.provide(TestClient.layer, zio.Scope.default)
  )
}