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
  def actorRefFactory = system  // connect the DSL to the test ActorSystem
  val host = "localhost"
  val port = 27017
  val mongo = MongoConnection(host,port)

  //clear data
  mongo("cuby-data")("players").remove(MongoDBObject.empty)
  mongo("cuby-data")("games").remove(MongoDBObject.empty)

  //add test data
  val player1 = Player(new ObjectId().toString, "Antonio Esfandiari", "a.esfandiari@poker.com", 35)
  val player2 = Player(new ObjectId().toString, "Sam Trickett", "s.trickett@poker.com", 26)
  val player3 = Player(new ObjectId().toString, "Erik Seidel", "e.seidel@poker.com", 53)
  val player4 = Player(new ObjectId().toString, "Daniel Negreanu", "d.negreanu@poker.com", 38)
  val player5 = Player(new ObjectId().toString, "Phil Ivey", "p.ivy@poker.com", 34)

  PlayerDAO.insert(player1)
  PlayerDAO.insert(player2)
  PlayerDAO.insert(player3)
  PlayerDAO.insert(player4)
  PlayerDAO.insert(player5)

  val game = Game(new ObjectId().toString, "A Pokergames", player1, List[Player](player1,player2,player3))
  GameDAO.insert(game)

  //some of the tests are sequential
  sequential

  "The service" should {

    "return the version number as 1.0" in {
      Get("/api/version") ~> restRoute ~> check {
        entityAs[Version] must be equalTo(Version("1.0"))
      }
    }

    "Player " + player1.name + " should be in the db" in {
      Get("/api/1.0/player/" + player1._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player1)
      }
    }
    "Player " + player2.name + " should be in the db" in {
      Get("/api/1.0/player/" + player2._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player2)
      }
    }
    "Player " + player3.name + " should be in the db" in {
      Get("/api/1.0/player/" + player3._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player3)
      }
    }
    "Player " + player4.name + " should be in the db" in {
      Get("/api/1.0/player/" + player4._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player4)
      }
    }
    "Player " + player5.name + " should be in the db" in {
      Get("/api/1.0/player/" + player5._id) ~> restRoute ~> check {
        entityAs[Player] must be equalTo(player5)
      }
    }

    "At the start there should be 5 players" in {
      Get("/api/1.0/player/") ~> restRoute ~> check {
        entityAs[List[Player]] must have size(5)
      }
    }

    "At the start there should be 1 game" in {
      Get("/api/1.0/game") ~> restRoute ~> check {
        entityAs[List[Game]] must have size(1)
      }
    }

    "Deleting " + player5.name + " should result in Ok" in {
      Delete("/api/1.0/player/" + player5._id) ~> restRoute ~> check {
        entityAs[Result] must be equalTo(Result("Ok"))
      }
    }

    "After deleting there should only be 4 players in the Db" in {
      Get("/api/1.0/player/") ~> restRoute ~> check {
        entityAs[List[Player]] must have size(4)
      }
    }

    "The Game should have 3 players" in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        entityAs[Game].players must have size(3)
      }
    }

    "Player " + player1.name + " should be the host" in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        entityAs[Game].host must be equalTo(player1)
      }
    }

    "When " + player4.name + " joins the result should say Ok" in {
      Get("/api/1.0/game/join/" + game._id + "/" + player4._id) ~> restRoute ~> check {
        entityAs[Result] must be equalTo(Result("Ok"))
      }
    }

    "there should be 4 players in the game" in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        entityAs[Game].players must have size(4)
      }
    }

    "and should contain " + player4.name in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        entityAs[Game].players must contain(player4)
      }
    }

    "When " + player2.name + " leaves the game the result should be Ok" in {
      Get("/api/1.0/game/leave/" + game._id + "/" + player2._id) ~> restRoute ~> check {
        entityAs[Result] must be equalTo(Result("Ok"))
      }
    }

    "There should now be 3 players in the game" in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        entityAs[Game].players must have size(3)
      }
    }
    "Player " + player2.name + " should not be in the game anymore" in {
      Get("/api/1.0/game/" + game._id) ~> restRoute ~> check {
        val p = entityAs[Game].players
        p must not contain(player2)
      }
    }

    "If the Host " + player1.name + " leaves the game result should be 'Host left, deleted game: " + game.name + "'" in {
      Get("/api/1.0/game/leave/" + game._id + "/" + player1._id) ~> restRoute ~> check {
        entityAs[Result] must be equalTo(Result("Host left, deleted game: " + game.name))
      }
    }

    "There should be no more games" in {
      Get("/api/1.0/game") ~> restRoute ~> check {
        entityAs[List[Game]] must have size(0)
      }
    }
  }
}