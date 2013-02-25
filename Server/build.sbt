name := "CubyServer"

organization := "net.addictivesoftware.cuby"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions += "-Ydependent-method-types"

resolvers ++= Seq(
    "Spray" at "http://repo.spray.io",
    "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    "Novus" at "http://repo.novus.com/releases/",
    "Novus Snapshots" at "http://repo.novus.com/snapshots/",
    "Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/"
)

libraryDependencies ++= Seq(
    "io.spray" % "spray-http" % "1.0-M7",
    "io.spray" % "spray-can" % "1.0-M7",
    "io.spray" % "spray-io" % "1.0-M7",
    "io.spray" % "spray-routing" % "1.0-M7",
    "io.spray" %  "spray-json_2.9.2" % "1.2.3",
    "com.typesafe.akka" % "akka-actor" % "2.0.4",
    "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"
)
