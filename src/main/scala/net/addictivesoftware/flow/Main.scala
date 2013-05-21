package net.addictivesoftware.flow

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService


object Main extends App with SprayCanHttpServerApp {
  val url:String = FlowProperties.getEnvOrProp("OPENSHIFT_APP_DNS")
  val applicationHost:String    = url.contains(":") match {
    case true => url.split(":")(0)
    case _ => url
  }
  val applicationPort:Int    = url.contains(":") match {
    case true => url.split(":")(1) toInt
    case _ => 80
  }

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = applicationHost, port = applicationPort)
}
