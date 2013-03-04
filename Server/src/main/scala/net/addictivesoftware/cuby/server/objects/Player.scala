package net.addictivesoftware.cuby.server.objects

import com.novus.salat._
import annotations.raw.Key
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import dao.SalatDAO

import spray.json._
import com.mongodb.casbah.commons.TypeImports.ObjectId
import scala.Some

//model
case class Player(@Key("_id") _id:String = new ObjectId().toString, name:String, email:String, age:Int) {}

//DAO
object PlayerDAO extends SalatDAO[Player, Int](collection = MongoConnection()("cuby-data")("players"))

object PlayerCRUD {
  def insert(player:Player):Option[Int] = {
   PlayerDAO.insert(player)
  }

  def delete(player:Player) = {
    PlayerDAO.remove(player)
  }

  def deleteById(id:String) = {
    getById(id) match {
      case Some(player) => {delete(player)}
      case _ => {}
    }
  }

  def update(id: String, player:Player) = {
    PlayerDAO.update(MongoDBObject("_id" -> id), grater[Player].asDBObject(player))

  }

  def getById(id:String):Option[Player] = {
    PlayerDAO.find(MongoDBObject("_id" -> id))
      .limit(1)
      .toList
      .headOption
  }

  def list() = {
    PlayerDAO.find(MongoDBObject()).toList
  }
}
