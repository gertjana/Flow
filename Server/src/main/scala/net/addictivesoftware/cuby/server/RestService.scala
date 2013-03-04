package net.addictivesoftware.cuby.server

import akka.actor.Actor
import objects.{GameCRUD, Game, PlayerCRUD, Player}
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import com.mongodb.casbah.Imports._
import spray.http.MediaTypes.`application/json`
import com.weiglewilczek.slf4s.{Logging, Logger}

//import implicit json formats
import spray.json.DefaultJsonProtocol._
import MyJsonProtocol._


//Simple cases classes that can be returned as Json
case class Version(version:String)
case class Result(result:String)
case class Error(error:String)

class RestService extends Actor with RoutingRestService {

  def actorRefFactory = context
  def receive = runRoute(restRoute)
}

trait RoutingRestService extends HttpService with SprayJsonSupport with Logging {
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
              logger.info("listing players")
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
                    logger.info("Added player with id: " + id)
                    Result(id.toString)
                  }
                  case _ => {
                    logger.error("unable to add player " + player)
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
                  logger.info("Found and returning player: " + player)
                    player
                }
                case (_) => {
                  logger.error("Player with ID: " + id + " not found")
                  Error("Player with ID: " + id + " not found")
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
                logger.info("updated player with ID: " + id + " with: " + player)
                Result("Ok")
              }
            }
          }
        } ~
        // delete a player
        delete {
          respondWithMediaType(`application/json`) {
            complete {
              PlayerCRUD.deleteById(id)
              logger.info("deleted player with ID " + id)
              Result("Ok")
            }
          }
        }
      } ~
      path("game" / "host" / "\\w+".r / "\\w+".r) {(name:String, player_id:String) =>
        // host a game
        get {
          respondWithMediaType(`application/json`) {
            complete {
              PlayerCRUD.getById(player_id) match {
                case Some(p) => {
                  GameCRUD.insert(Game(new ObjectId().toString, name, p, p :: Nil))  match {
                    case Some(id) => {
                      logger.info("player " + name + "is hosting a game with ID:" + id )
                      Result(id.toString)
                    }
                    case _ =>  {
                      logger.error("unable to add game " + name )
                      Error("unable to add game " + name )
                    }
                  }
                }
                case _ => {
                  logger.error("Player with ID: " + player_id + " not found")
                  Error("Player with ID: " + player_id + " not found")
                }
              }
            }
          }
        }
      } ~
      path ("game" / "join" / "\\w+".r / "\\w+".r) { (game_id:String, player_id:String) =>
        // join a game
        get {
          respondWithMediaType(`application/json`) {
            complete {
              GameCRUD.getById(game_id) match {
                case Some(game) => {
                  PlayerCRUD.getById(player_id) match {
                    case Some(player) => {
                      if (game.players.contains(player)) {
                        logger.warn("Player with ID: " + player_id + " already joined")
                        Result("Player with ID: " + player_id + " already joined")
                      } else {
                        GameCRUD.update(game_id, game.copy(players = player :: game.players))
                        logger.info("player: " + player.name + " joined game: " + game.name)
                        Result("Ok")
                      }
                    }
                    case _ => {
                      logger.error("Player with ID: " + player_id + " not found")
                      Error("Player with ID: " + player_id + " not found")
                    }
                  }
                }
                case _ => {
                  logger.error("Game with ID: " + game_id + " not found")
                  Error("Game with ID: " + game_id + " not found")
                }
              }
            }
          }
        }
      } ~
      path("game" / "leave" / "\\w+".r / "\\w+".r) { (game_id:String, player_id:String) =>
        //leave a game
        get {
          respondWithMediaType(`application/json`) {
            complete {
              GameCRUD.getById(game_id) match {
                case Some(game) => {
                  PlayerCRUD.getById(player_id) match {
                    case Some(player) => {
                      if (player == game.host) {
                        //delete game when host leaves
                        GameCRUD.delete(game)
                        logger.info("Host left, deleted game: " + game.name)
                        Result("Host left, deleted game: " + game.name)
                      } else {
                        val restPlayers = game.players.filter(_ != player)
                        GameCRUD.update(game_id, game.copy(players = restPlayers))
                        logger.info("player: " + player.name + " left game: " + game.name)
                        Result("Ok")
                      }
                    }
                    case _ => {
                      logger.error("Player with ID: " + player_id + " not found")
                      Error("Player with ID: " + player_id + " not found")
                    }
                  }
                }
                case _ => {
                  logger.error("Game with ID: " + game_id + " not found")
                  Error("Game with ID: " + game_id + " not found")
                }
              }
            }
          }
        }
      } ~
      path ("game") {
        //list games
        get {
          respondWithMediaType(`application/json`) {
            complete {
              logger.info("Listing games")
              GameCRUD.list
            }
          }
        }
      } ~
      path ("game" / "\\w+".r) { game_id:String =>
        //get a single game
        get {
          respondWithMediaType(`application/json`) {
            complete {
              GameCRUD.getById(game_id) match {
                case Some(game) => {
                  logger.info("Found and returning game: " + game_id)
                  game
                }
                case _ => {
                  logger.error("Game with ID: " + game_id + " not found")
                  Error("Game with ID: " + game_id + " not found")
                }
              }
            }
          }
        }
      }
    }
  }
}