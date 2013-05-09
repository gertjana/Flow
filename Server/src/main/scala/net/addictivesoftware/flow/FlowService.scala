package net.addictivesotware.flow

import akka.actor.{Props, Actor}
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import com.weiglewilczek.slf4s.Logging
import scala.util.Random
import spray.http.MediaTypes.`text/html`
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.flow.{EventActor, RecordEvent}
import net.addictivesoftware.flow.objects.WebEvent
import spray.http.FormData

class FlowService extends Actor with FlowRoutingService {
  def actorRefFactory = context
  def receive = runRoute(flowRoute)
}

trait FlowRoutingService extends HttpService with SprayJsonSupport with Logging {
  val eventActor = actorRefFactory.actorOf(Props[EventActor])
  val Ok = "Ok"

  val flowRoute = pathPrefix("flow") {
    path("index") {    // returns helphtml
      get {
        respondWithMediaType(`text/html`) {
          complete {
            index
          }
        }
      }
    } ~
    path("list") {    //list all events
      get {
        respondWithMediaType(`text/html`) {
          complete {
            list
          }
        }
      }
    } ~
    path("getsession") {    //return a session id, TODO check/set cookie for id
      get {
        complete {
          new Random().nextLong().toString()
        }
      }
    } ~
    path("event" / "\\w+".r / "\\w+".r) { (session:String, event:String) =>     //records event
      post {
        entity(as[FormData]) { formData:FormData =>
          complete {
            eventActor ! RecordEvent(WebEvent(session=session, event=event, data=formData.fields))
            Ok
          }
        }
      }
    }
  }

  private val index =
    <html>
      <head>
        <title>Flow - Index</title>
      </head>
      <body>
          Flow: a "Non Blocking" webservice to record events:
          <table border="1">
            <tr>
              <th>What?</th>
              <th>Method</th>
              <th>Url</th>
              <th>Description</th>
            </tr>
            <tr>
              <td>This file</td>
              <td>GET</td>
              <td><a href="/flow/index">/flow/index</a></td>
              <td></td>
            </tr>
             <tr>
               <td>Get a session ID</td>
               <td>GET</td>
               <td><a href ="/flow/getsession">/flow/getsession</a></td>
               <td><i>This is optional, it's fine to pass on the session id from the webserver</i></td>
             </tr>
            <tr>
              <td>Record an Event</td>
              <td>POST</td>
              <td><a href="/flow/event/[session]/[event-name]">/flow/event/[session]/[event-name]</a></td>
              <td>post parameters will also be stored</td>
            </tr>
            <tr>
              <td>List Events</td>
              <td>GET</td>
              <td><a href="/flow/list">/flow/list</a></td>
              <td>List currently stored events</td>
            </tr>
           </table>
      </body>
    </html>

  private val list =
    <html>
      <head>
        <title>Flow - List Events</title>
      </head>
      <body>
        List Events:
        <table border="1">
          <tr>
            <th>TimeStamp</th>
            <th>id</th>
            <th>Session</th>
            <th>Event</th>
            <th>Data</th>
          </tr>
          {
            WebEvent.list().map(
              event => {
                <tr>
                  <td>{event.timestamp}</td>
                  <td>{event._id}</td>
                  <td>{event.session}</td>
                  <td>{event.event}</td>
                  <td>{event.data.toString()}</td>
                </tr>
              }
            )
          }
        </table>
      </body>
    </html>


}
