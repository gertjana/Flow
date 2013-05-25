package net.addictivesoftware.flow

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService


object Main extends App with SprayCanHttpServerApp {
  val applicationHost:String = FlowProperties.getEnvOrProp("OPENSHIFT_APP_DNS")
  val applicationPort:Int    = FlowProperties.getEnvOrProp("OPENSHIFT_INTERNAL_PORT") toInt

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = applicationHost, port = applicationPort)
}
