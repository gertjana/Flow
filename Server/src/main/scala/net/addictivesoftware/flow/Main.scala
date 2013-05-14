package net.addictivesoftware.flow

import akka.actor.{ActorSystem, Props}
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService

object Main extends App with SprayCanHttpServerApp {
  val mongo = MongoConnection("localhost",27017)

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = "devgertjana.global.sdl.corp", port = 9999)
}
