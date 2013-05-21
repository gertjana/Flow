package net.addictivesoftware.flow

import com.mongodb.casbah.Imports._

object FlowMongoConnection {
  val mongoHost             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_HOST")
  val mongoPort             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT") toInt
  val mongoUser             = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USER") 
  val mongoPassword         = FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PASSWORD")
  val authRequired:Boolean  = true

  def flowCollection = {
    val db = MongoConnection(mongoHost, mongoPort).getDB("flow")
    if (authRequired) {
      db.authenticate(mongoUser, mongoPassword)
    }
    db("events")
  }
}