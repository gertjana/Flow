package net.addictivesoftware.cuby.server

import akka.actor.{ActorLogging, Actor}
import spray.http.{HttpRequest, HttpResponse}
import spray.http.HttpMethods._
import akka.util.duration._

class DemoService extends Actor with ActorLogging {
  def receive = {
    case HttpRequest(GET, "/ping", _, _, _) => {
        sender ! HttpResponse(200, "pong")
    }
    case HttpRequest(GET, "/stop", _, _, _) => {
      sender ! HttpResponse(entity = "Shutting down in 1 second ...")
      context.system.scheduler.scheduleOnce(1 seconds, new Runnable { def run() { context.system.shutdown() } })
    }
    case _ => {
      sender ! HttpResponse(200, "received a unhandled request")
    }
  }
}