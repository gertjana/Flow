package net.addictivesoftware.flow.objects

import com.novus.salat.annotations.raw.Key
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import scala.Some
import net.addictivesoftware.flow.FlowMongoConnection

case class EventObject (
  @Key("_id") _id:String = new ObjectId().toString,
  timestamp:Long = System.currentTimeMillis,
  session:String,
  event:String,
  data:Map[String, String]
)

object WebEventDAO extends SalatDAO[EventObject, String](collection = FlowMongoConnection.flowCollection)

object WebEvent {

  def insert(event:EventObject):Option[String] = {
    WebEventDAO.insert(event)
  }

  def delete(event:EventObject) = {
    WebEventDAO.remove(event)
  }

  def deleteById(id:String) = {
    getById(id) match {
      case Some(event) => { delete(event) }
      case _ => {}
    }
  }

  def update(id: String, event:EventObject) = {
    WebEventDAO.update(MongoDBObject("_id" -> id), grater[EventObject].asDBObject(event))
  }

  def getById(id:String):Option[EventObject] = {
    WebEventDAO.find(MongoDBObject("_id" -> id))
      .limit(1)
      .toList
      .headOption
  }

  def list():List[EventObject] = {
    WebEventDAO.find(MongoDBObject()).toList
  }
}