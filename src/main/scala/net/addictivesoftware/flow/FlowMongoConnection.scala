package net.addictivesoftware.flow

import com.mongodb.casbah.Imports._

object FlowMongoConnection {
  val mongoUser       = Option(System.getenv("OPENSHIFT_MONGO_DB_USER")) getOrElse FlowProperties.getString("mongo-user")
  val mongoPassword   = Option(System.getenv("OPENSHIFT_MONGO_DB_PASSWORD")) getOrElse FlowProperties.getString("mongo-password")
  val authRequired:Boolean = true

  def flowCollection = {
    val db = MongoConnection().getDB("flow")
    if (authRequired) {
      db.authenticate(mongoUser, mongoPassword)
    }
    db("events")
  }
}