package net.addictivesotware.flow

import akka.actor.{Props, Actor}
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import com.weiglewilczek.slf4s.Logging
import scala.util.Random
import spray.http.MediaTypes.{`text/html`, `application/javascript`, `text/css`}
import net.addictivesoftware.flow.{WebPages, EventActor, RecordEvent}
import net.addictivesoftware.flow.objects.WebEvent
import spray.http.FormData


//implicit marshallers/unmarshallers
import spray.json.DefaultJsonProtocol._

/**
 * Actor that recieves requests and executes the mixed-in route
 */
class FlowService extends Actor with FlowRoutingService {
  def actorRefFactory = context
  def receive = runRoute(flowRoute)
}

  /**
   * 
   * Trait that contains the route to execute
   */
trait FlowRoutingService extends HttpService with WebPages with SprayJsonSupport with Logging {
  val eventActor = actorRefFactory.actorOf(Props[EventActor])
  val Ok = "Ok"

  val flowRoute = 
  pathPrefix("flow") {
    path("index") {    // returns helphtml
      get {
        respondWithMediaType(`text/html`) {
          complete {
            index
          }
        }
      }
    } ~
    path ("\\w+.html".r) {filename => // serve static html files
      get {
        respondWithMediaType(`text/html`) {
          getFromResource(filename)
        }
      }
    } ~
    path("js" / "[\\w\\.]+".r) { filename => // serve static javascript files
      get {
        respondWithMediaType(`application/javascript`) {
          getFromResource("js/" + filename);
        }
      }
    } ~
    path("css" / "[\\w\\.]+".r) { filename => // server static css
      get {
        respondWithMediaType(`text/css`) {
          getFromResource("css/" + filename);
        }
      }
    } ~
    path("list") {    //list all events
      get {
        respondWithMediaType(`text/html`) {
          complete {
            list(WebEvent.list())
          }
        }
      }
    } ~    
    path("list" / "\\w+".r) { session =>    //list all events for a session id
      get {
        respondWithMediaType(`text/html`) {
          complete {
            list(WebEvent.list().filter(_.session == session))
          }
        }
      }
    } ~
    path("getsession") {    //return a session id, TODO check/set cookie for id
      get {
        complete {
          scala.Math.abs(new Random().nextLong()).toString()
        }
      }
    } ~
    path("event" / "\\w+".r / "\\w+".r) { (session:String, event:String) =>     //records event
      post {
        entity(as[FormData]) { formData:FormData =>
          complete {
            eventActor ! RecordEvent(session, event, formData.fields)
            Ok
          }
        }
      }
    }
  }
}

