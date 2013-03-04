package net.addictivesoftware.cuby.server.objects

import com.novus.salat.annotations.raw.Key
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

import com.mongodb.casbah.commons.TypeImports.ObjectId

case class Game(@Key("_id") _id:String = new ObjectId().toString, name:String, host:Player, players:List[Player]) {}

//DAO
object GameDAO extends SalatDAO[Game, Int](collection = MongoConnection()("cuby-data")("games"))

object GameCRUD {

  def insert(game:Game):Option[Int] = {
   GameDAO.insert(game)
  }

  def delete(game:Game) = {
   GameDAO.remove(game)
  }

 def deleteById(id:String) = {
  getById(id) match {
    case Some(game) => { delete(game) }
    case _ => {}
  }
 }

 def update(id: String, game:Game) = {
  GameDAO.update(MongoDBObject("_id" -> id), grater[Game].asDBObject(game))
 }

 def getById(id:String):Option[Game] = {
  GameDAO.find(MongoDBObject("_id" -> id))
    .limit(1)
    .toList
    .headOption
 }

 def list() = {
  GameDAO.find(MongoDBObject()).toList
 }

}

