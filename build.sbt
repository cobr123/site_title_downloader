ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

name := "site_title_downloader"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-http" % "3.0.1",
  "io.circe" %% "circe-parser" % "0.14.10",
  "org.jsoup" % "jsoup" % "1.18.3"
)
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % "2.1.9" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.9" % Test,
  "dev.zio" %% "zio-http-testkit" % "3.0.1" % Test
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")