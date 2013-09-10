package net.addictivesoftware.flow

import com.mongodb.casbah.Imports._

object FlowMongoConnection {
  val mongoHost             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_HOST")
  val mongoPort             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT") toInt
  val mongoUser             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USERNAME") 
  val mongoPassword         = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PASSWORD")

  def getEventsCollection() = {
    val db = MongoConnection(mongoHost, mongoPort).getDB("flow")
    try {
      db.authenticate(mongoUser, mongoPassword)
    } catch {
      case _:MongoException => {} // ignoring authentication exception when authentication is turned off on the mongodb server
    }

    db("events")
  }
}