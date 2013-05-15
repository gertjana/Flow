package net.addictivesoftware.flow

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService


object Main extends App with SprayCanHttpServerApp {
  val mongo = MongoConnection(FlowProperties.getString("mongo-host"),FlowProperties.getInt("mongo-port"))

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = FlowProperties.getString("application-host"), port = FlowProperties.getInt("application-port"))
}
