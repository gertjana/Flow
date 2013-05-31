package net.addictivesoftware.flow

import org.specs2.mutable._

class FlowPropertiesSpec extends Specification {

  "The Flow Properties class" should {

    "loading flowProperties should have 7 items" in {
      FlowProperties.flowProperties.size mustEqual 7
    }

    "loading OPENSHIFT_MONGODB_DB_PORT should result in 27017" in {
      FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT").toInt mustEqual 27017
    }
  }
}