package net.addictivesoftware.cuby.server

import akka.actor.{ActorSystem, Props}
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._

object Main extends App with SprayCanHttpServerApp {

  // the handler actor replies to incoming HttpRequests
  val handler = system.actorOf(Props[RestService])

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(handler) ! Bind(interface = "localhost", port = 8080)

}