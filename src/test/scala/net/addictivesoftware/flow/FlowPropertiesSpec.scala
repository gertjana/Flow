package net.addictivesoftware.flow

import org.specs2.mutable._

class FlowPropertiesSpec extends Specification {

  "The Flow Properties class" should {

    "loading flowProperties should have 7 items" in {
      FlowProperties.flowProperties.size mustEqual 7
    }

    "loading some OPENSHIFT_* should result in the correct values" in {
      FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT").toInt mustEqual 27017
      FlowProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USERNAME") mustEqual "admin"
   }

    "loading an env variable should work fine" in {
    	FlowProperties.getEnvOrProp("HOME") must not beNull
    }
  }
}