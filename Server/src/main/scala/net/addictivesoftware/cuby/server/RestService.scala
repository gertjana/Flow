package net.addictivesoftware.cuby.server

import akka.actor.Actor
import objects.{GameCRUD, Game, PlayerCRUD, Player}
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import com.mongodb.casbah.Imports._
import spray.json.{JsValue, JsString, JsonFormat}
import spray.json.DefaultJsonProtocol._
import spray.http.StatusCodes.NotImplemented
import spray.http.MediaTypes.`application/json`

//import implicit json formats
import MyJsonProtocol._

class RestService extends Actor with RoutingRestService {
  def actorRefFactory = context
  def receive = runRoute(restRoute)
}

//Simple cases classes that can be returned as Json
case class Version(version:String)
case class Result(result:String)
case class Error(error:String)


trait RoutingRestService extends HttpService with SprayJsonSupport {
  val apiVersion = "1.0"

  val restRoute = pathPrefix("api") {
    path("version") {
      //gets the current version of the api
      get {
        respondWithMediaType(`application/json`) {
          complete {
            Version(apiVersion)
          }
        }
      }
    } ~
    pathPrefix(apiVersion) {
      path("player") {
        //gets a list of all players
        get {
          respondWithMediaType(`application/json`) {
            complete {
              PlayerCRUD.list
            }
          }
        } ~
        //adds a new player
        put {
          entity(as[Player]) { player:Player =>
            respondWithMediaType(`application/json`) {
              complete {
                PlayerCRUD.insert(player) match {
                  case Some(id) => {
                    Result(id.toString)
                  }
                  case _ => {
                    Error("unable to add player " + player)
                  }
                }
              }
            }
          }
        }
      } ~
      path("player" / "\\w+".r) {id:String =>
        //get a player by its id
        get {
          respondWithMediaType(`application/json`) {
            complete {
              PlayerCRUD.getById(id) match {
                case Some(player) => {
                    player
                }
                case (_) => {
                  Error("Player with Id " + id + " not found")
                }
              }
            }
          }
        } ~
        //updates a player
        post {
          entity(as[Player]) {player:Player =>
            respondWithMediaType(`application/json`) {
              complete {
                PlayerCRUD.update(id, player)
                Result("OK")
              }
            }
          }
        } ~
        // delete a player
        delete {
          respondWithMediaType(`application/json`) {
            complete {
              Result(PlayerCRUD.deleteById(id).toString)
            }
          }
        }
      } ~
      pathPrefix("game") {
        path("host" / "\\w+".r / "\\w+".r) {(name:String, player_id:String) =>
          // host a game
          get {
            respondWithMediaType(`application/json`) {
              complete {
                PlayerCRUD.getById(player_id) match {
                  case Some(p) => {
                    GameCRUD.insert(Game(new ObjectId(), name, p, p :: Nil))  match {
                      case Some(id) => Result(id.toString)
                      case _ => Error("unable to add game " + name )
                    }
                  }
                  case _ => Error("Player with ID: " + player_id + " not found")
                }
              }
            }
          }
        } ~
        path ("list") {
          //list games
          get {
            respondWithMediaType(`application/json`) {
              complete {
                GameCRUD.list
              }
            }
          }
        } ~
        path ("join" / "\\w+".r / "\\w+".r) { (game_id:String, player_id:String) =>
          // join a game
          get {
            respondWithMediaType(`application/json`) {
              complete {
                GameCRUD.getById(game_id) match {
                  case Some(game) => {
                    PlayerCRUD.getById(player_id) match {
                      case Some(player) => {
                        GameCRUD.update(game_id, game.copy(players = player :: game.players))
                        Result("OK")
                      }
                      case _ => Error("Player with ID: " + player_id + " not found")
                    }
                  }
                  case _ => Error("Game with ID: " + game_id + " not found")
                }
              }
            }
          }
        } ~
        path("leave" / "\\w+".r / "\\w+".r) { (game_id:String, player_id:String) =>
          get {
            respondWithMediaType(`application/json`) {
              complete {
                GameCRUD.getById(game_id) match {
                  case Some(game) => {
                    PlayerCRUD.getById(player_id) match {
                      case Some(player) => {
                        GameCRUD.update(game_id, game.copy(players = game.players.filter(_ != player)))
                        Result("OK")
                      }
                      case _ => Error("Player with ID: " + player_id + " not found")
                    }
                  }
                  case _ => Error("Game with ID: " + game_id + " not found")
                }
              }
            }
          }
        } ~
        path ("\\w+".r) { game_id:String =>
          delete {
            respondWithMediaType(`application/json`) {
              complete {
                Result(GameCRUD.deleteById(game_id).toString)
              }
            }
          }
        }
      }
    }
  }
}