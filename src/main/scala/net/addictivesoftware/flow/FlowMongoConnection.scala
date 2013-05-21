package net.addictivesoftware.flow

import com.mongodb.casbah.Imports._

object FlowMongoConnection {
  val mongoHost = Option(System.getenv("OPENSHIFT_MONGODB_DB_HOST")) getOrElse FlowProperties.getString("mongo-host")
  val mongoPort = Option(System.getenv("OPENSHIFT_MONGODB_DB_PORT")) getOrElse FlowProperties.getString("mongo-port") toInt
  val mongoUser       = Option(System.getenv("OPENSHIFT_MONGODB_DB_USER")) getOrElse FlowProperties.getString("mongo-user")
  val mongoPassword   = Option(System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD")) getOrElse FlowProperties.getString("mongo-password")
  val authRequired:Boolean = true

  def flowCollection = {
    val db = MongoConnection(mongoHost, mongoPort).getDB("flow")
    if (authRequired) {
      db.authenticate(mongoUser, mongoPassword)
    }
    db("events")
  }
}