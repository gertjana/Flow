package net.addictivesoftware.cuby.server

import objects.{Game, Player}
import spray.json.{JsValue, JsString, JsonFormat}
import com.mongodb.casbah.commons.TypeImports.ObjectId

//import implicit base json formats
import spray.json.DefaultJsonProtocol._

object MyJsonProtocol {

  implicit val PlayerFormat = jsonFormat(Player, "_id", "name", "email", "age")

  implicit val GameFormat = jsonFormat(Game, "_id", "name", "host", "players")

  implicit val VersionFormat = jsonFormat(Version, "version")

  implicit val ResultFormat = jsonFormat(Result, "result")

  implicit val ErrorFormat = jsonFormat(Error, "error")
}
