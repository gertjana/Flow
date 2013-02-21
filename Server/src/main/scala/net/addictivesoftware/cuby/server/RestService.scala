package net.addictivesoftware.cuby.server

import akka.actor.Actor
import spray.routing.HttpService
import spray.http.MediaTypes._
import akka.util.duration._

class RestService extends Actor with RoutingRestService {
  def actorRefFactory = context

  def receive = runRoute(restRoute)
}

trait RoutingRestService extends HttpService {
  val restRoute = {
    get {
      path("version") {
        respondWithMediaType(`application/json`) {
          complete {
            """{"version":"0.1.0" }"""
          }
        }
      }
    }
  }
}