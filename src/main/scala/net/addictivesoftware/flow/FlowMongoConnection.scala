package net.addictivesoftware.flow

import com.mongodb.casbah.Imports._

object FlowMongoConnection {
  val mongoHost             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_HOST")
  val mongoPort             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT") toInt
  val mongoUser             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USER") 
  val mongoPassword         = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PASSWORD")
  
  def flowCollection = {
    val db = MongoConnection(mongoHost, mongoPort).getDB("flow")
    try {
      db.authenticate(mongoUser, mongoPassword)
    } catch {
      case _ => {}//do nothing
    }

    db("events")
  }
}