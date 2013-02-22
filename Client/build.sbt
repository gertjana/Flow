name := "CubyClient"

organization := "net.addictivesoftware.cuby"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions += "-Ydependent-method-types"

resolvers ++= Seq(
    "Spray Repo" at "http://repo.spray.io",
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-swing" % "2.9.2"
)
