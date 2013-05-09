package net.addictivesoftware.cuby.server

import akka.actor.{ActorSystem, Props}
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService

object Main extends App with SprayCanHttpServerApp {
  val host = "localhost"
  val port = 27017
  val mongo = MongoConnection(host,port)

  //val restHandler = system.actorOf(Props[RestService])
  //newHttpServer(restHandler) ! Bind(interface = "localhost", port = 8080)

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = "localhost", port = 9999)

}