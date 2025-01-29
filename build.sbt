ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

name := "site_title_downloader"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-http" % "3.0.1",
  "io.circe" %% "circe-parser" % "0.14.10",
  "org.jsoup" % "jsoup" % "1.18.3"
)