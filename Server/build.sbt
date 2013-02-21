name := "CubyServer"

organization := "net.addictivesoftware.cuby"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions += "-Ydependent-method-types"

resolvers ++= Seq(
    "Spray Repo" at "http://repo.spray.io",
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
    "io.spray" % "spray-http" % "1.0-M7",
    "io.spray" % "spray-can" % "1.0-M7",
    "io.spray" % "spray-io" % "1.0-M7",
    "io.spray" % "spray-routing" % "1.0-M7",
    "io.spray" %%  "spray-json" % "1.2.3",
    "com.typesafe.akka" % "akka-actor" % "2.0.4"
)
