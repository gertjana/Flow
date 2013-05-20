package net.addictivesoftware.flow

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import com.mongodb.casbah.Imports._
import net.addictivesotware.flow.FlowService


object Main extends App with SprayCanHttpServerApp {
  //if we're on openshift take those values else from properties file
//  val mongoHost = Option(System.getenv("OPENSHIFT_MONGODB_DB_HOST")) getOrElse FlowProperties.getString("mongo-host")
//  val mongoPort = Option(System.getenv("OPENSHIFT_MONGODB_DB_PORT")) getOrElse FlowProperties.getString("mongo-port") toInt

// val mongo = MongoConnection(mongoHost, mongoPort)
  
  val applicationHost = Option(System.getenv("OPENSHIFT_INTERNAL_IP"))   getOrElse FlowProperties.getString("application-host")
  val applicationPort = Option(System.getenv("OPENSHIFT_INTERNAL_PORT")) getOrElse FlowProperties.getString("application-port") toInt

  val flowHandler = system.actorOf(Props[FlowService])
  newHttpServer(flowHandler) ! Bind(interface = applicationHost, port = applicationPort)
}
