name := "remix-core"
version := "0.1.0"
scalaVersion := "2.12.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)

routesGenerator := InjectedRoutesGenerator

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += guice
