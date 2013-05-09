package net.addictivesoftware.flow.objects
import com.novus.salat.annotations.raw.Key
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import scala.Some

case class WebEvent(
  @Key("_id") _id:String = new ObjectId().toString,
  timestamp:Long = System.currentTimeMillis,
  session:String,
  event:String,
  data:Map[String, String]
)

object WebEventDAO extends SalatDAO[WebEvent, Int](collection = MongoConnection()("flow-data")("events"))

object WebEvent {

  def insert(event:WebEvent):Option[Int] = {
    WebEventDAO.insert(event)
  }

  def delete(event:WebEvent) = {
    WebEventDAO.remove(event)
  }

  def deleteById(id:String) = {
    getById(id) match {
      case Some(event) => { delete(event) }
      case _ => {}
    }
  }

  def update(id: String, event:WebEvent) = {
    WebEventDAO.update(MongoDBObject("_id" -> id), grater[WebEvent].asDBObject(event))
  }

  def getById(id:String):Option[WebEvent] = {
    WebEventDAO.find(MongoDBObject("_id" -> id))
      .limit(1)
      .toList
      .headOption
  }

  def list():List[WebEvent] = {
    WebEventDAO.find(MongoDBObject()).toList
  }
}