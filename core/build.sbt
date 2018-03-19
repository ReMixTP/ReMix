name := "remix-core"
version := "0.1.0"
scalaVersion := "2.12.4"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

lazy val root = (project in file(".")).enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)

routesGenerator := InjectedRoutesGenerator

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "3.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.44"
