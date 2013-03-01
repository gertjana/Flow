package net.addictivesoftware.cuby.server


import com.mongodb.casbah.commons.TypeImports.ObjectId
import com.mongodb.casbah.Imports._
import objects.{Game, Player, PlayerDAO, GameDAO}
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

// import implicit json formats
import MyJsonProtocol._
import spray.json.DefaultJsonProtocol._


class RestServiceSpec extends Specification with Specs2RouteTest with RoutingRestService {
  def actorRefFactory = system // connect the DSL to the test ActorSystem
  val host = "localhost"
  val port = 27017
  val mongo = MongoConnection(host,port)

  //clear data
  mongo("cuby-data")("players").remove(MongoDBObject.empty)
  mongo("cuby-data")("games").remove(MongoDBObject.empty)

  //add test data
  val player1 = Player(new ObjectId(), "Antonio Esfandiari", "a.esfandiari@poker.com", 35)
  val player2 = Player(new ObjectId(), "Sam Trickett", "s.trickett@poker.com", 26)
  val player3 = Player(new ObjectId(), "Erik Seidel", "e.seidel@poker.com", 53)
  val player4 = Player(new ObjectId(), "Daniel Negreanu", "d.negreanu@poker.com", 38)
  val player5 = Player(new ObjectId(), "Phil Ivey", "p.ivy@poker.com", 34)

  PlayerDAO.insert(player1)
  PlayerDAO.insert(player2)
  PlayerDAO.insert(player3)
  PlayerDAO.insert(player4)
  PlayerDAO.insert(player5)

  val game = Game(new ObjectId(), "A Pokergames", player1, List[Player](player1,player2,player3))
  GameDAO.insert(game)

  "The service" should {

    "return the version number as 1.0" in {
      Get("/api/version") ~> restRoute ~> check {
        entityAs[Version] must be equalTo(Version("1.0"))
      }
    }

    "Player 'Antonio Esfandiari' should be in the db" in {
      Get("/api/1.0/player/" + player1._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player1)
      }
    }

    "At the start there should be 5 players" in {
      Get("/api/1.0/player/") ~> restRoute ~> check {
        entityAs[List[Player]] must have size(5)
      }
    }

    "At the start there should be 1 game" in {
      Get("/api/1.0/game/list") ~> restRoute ~> check {
        entityAs[List[Game]] must have size(1)
      }
    }
  }
}