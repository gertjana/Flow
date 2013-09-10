package net.addictivesotware.flow

import akka.actor.{Props, Actor}
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import com.weiglewilczek.slf4s.Logging
import scala.util.Random
import spray.http.MediaTypes.{`text/html`, `application/javascript`, `text/css`, `application/json`}
import net.addictivesoftware.flow.{FlowDirectives, WebPages, EventActor, RecordEvent}
import net.addictivesoftware.flow.objects.{WebEvent, EventObject}
import spray.http.{FormData, HttpHeaders, HttpCookie}
import HttpHeaders.`Set-Cookie` 

//implicit marshallers/unmarshallers
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.flow.MyJsonProtocol._


/**
 * Actor that recieves requests and executes the mixed-in route
 */
class FlowService extends Actor with FlowRoutingService {
  def actorRefFactory = context
  def receive = runRoute(flowRoute)
}


/**
 * Trait that contains the route to execute
 */
trait FlowRoutingService extends HttpService with WebPages with FlowDirectives with SprayJsonSupport with Logging {
  val eventActor = actorRefFactory.actorOf(Props[EventActor])
  val Ok = "Ok"

  val flowRoute = pathPrefix("flow") {
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
          getFromResourceAndReplacePlaceHolders("js/" + filename)
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
            listHtmlPage(WebEvent.list())
          }
        }
      }
    } ~    
    path("list" / "\\w+".r) { session =>    //list all events for a session id
      get {
        respondWithMediaType(`text/html`) {
          complete {
            listHtmlPage(WebEvent.list().filter(_.session == session))
          }
        }
      }
    } ~
    path("getsession") {    
      get {
        optionalCookie("flow-session") {cookieOption =>
          cookieOption match {
            case Some(cookie) => {
              respondWithHeader(`Set-Cookie`(cookie)) {
                complete {
                  cookie.content
                }
              }
            }
            case _ => {
              val newCookie = new HttpCookie("flow-session", scala.math.abs(new Random().nextLong()).toString())
              respondWithHeader(`Set-Cookie`(newCookie)) {
                complete {
                  newCookie.content
                }
              }                
            }
          }
        }
      }
    } ~
    path("event" / "\\w+".r / "\\w+".r) { (session:String, event:String) =>
      post {
        entity(as[FormData]) { formData:FormData =>
          complete {
            eventActor ! RecordEvent(session, event, formData.fields)
            Ok
          }
        }
      }
    } ~
    pathPrefix("api") {
      pathPrefix("events") {
        path("list") {
          get {
            respondWithMediaType(`application/json`) {
              complete {
                WebEvent.list()
              }
            }
          }
        } ~
        path("list" / "session" / "\\w+".r) {session =>
          get {
            respondWithMediaType(`application/json`) {
              complete {
                WebEvent.list().filter(_.session == session)
              }
            }
          }
        } ~
        path("single" / "\\w+".r ) {id =>
          get {
            respondWithMediaType(`application/json`) {
              complete {
                WebEvent.getById(id) match {
                  case Some(eventObject:EventObject) => eventObject
                  case _ => "EventObject not found"
                }
              }
            }
          }
        } ~
        path("single") {
          put {
            entity(as[EventObject]) {eventObject:EventObject =>
              complete {
                WebEvent.insert(eventObject) match {
                  case Some(id) => id
                  case _ => "Unable to add event"
                }
              }
            }
          }
        }
      }
    }
  }
}
