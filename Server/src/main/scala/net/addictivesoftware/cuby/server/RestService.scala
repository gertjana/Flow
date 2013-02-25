package net.addictivesoftware.cuby.server

import akka.actor.Actor
import objects.Player
import spray.routing.HttpService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol._

case class Version(version:String)



class RestService extends Actor with RoutingRestService {
  def actorRefFactory = context
  def receive = runRoute(restRoute)
}

trait RoutingRestService extends HttpService with SprayJsonSupport {
  val version = "0.1.0"
  implicit val VersionFormat = jsonFormat1(Version)
  implicit val PlayerFormat = jsonFormat3(Player)

  val restRoute = pathPrefix("api") {
    path("version") {
      //gets the current version of the api
      get {
        respondWithMediaType(`application/json`) {
          complete {
            Version(version)
          }
        }
      }
    } ~
    pathPrefix(version) {
      path("player") {
        //adds a new player
        put {
          entity(as[Player]) { player =>
            complete("Got a Player:" + player)
          }
        }
      } ~
      path("player" / "\\d+".r) {id =>
        //get a player by its id
        get {
          respondWithMediaType(`application/json`) {
            complete {
              Player("Gertjan Assies", "gertjan.assies@gmail.com", 43)
            }
          }
        } ~
        //updates a player
        post {
          entity(as[Player]) {player =>
            complete("Updating player :" + player + "with id: " + id)
          }
        } ~
        // delete a player
        delete {
            complete("Delete player with id: " + id)
        }
      } ~
      pathPrefix("game") {
        path("host") {
          respondWithStatus(NotImplemented) {
            complete("Not implemented (yet)")
          }
        } ~
        path ("list") {
          respondWithStatus(NotImplemented) {
            complete("Not implemented (yet)")
          }
        } ~
        path ("join" / "\\d+".r) { id =>
          respondWithStatus(NotImplemented) {
            complete("Not implemented (yet), but got an id of " + id.toString)
          }
        }
      }
    }
  }
}