package net.addictivesoftware.flow

import java.util.Properties
import java.io.IOException
import com.weiglewilczek.slf4s.Logging

object FlowProperties extends Logging with Utilities {
  var propFilename = "/flow.properties"
  val hostName = Option(java.net.InetAddress.getLocalHost().getHostName)


  def getString(name:String): String = {
    flowProperties.getProperty(name)
  }

  def getInt(name:String): Int = {
    flowProperties.getProperty(name).toInt
  }

  def getEnvOrProp(name: String) : String = {
    Option(System.getenv(name)) match {
      case Some(value) =>
        value
      case _ =>
        getString(name)
    }
  }


 lazy val flowProperties: java.util.Properties = {
    val properties = new java.util.Properties

    hostName match {
      case Some(name) => propFilename = "/flow-" + name.toLowerCase + ".properties"
      case _ => {}
    }
    var fileExists:Boolean = false;
    using( getClass.getResourceAsStream(propFilename) ) {stream => 
      try {
        properties.load(stream)
        fileExists = true;
      } catch {
        case _: Throwable => {
        }
      }
    }
    if (!fileExists) {
      using( getClass.getResourceAsStream("/flow.properties") ) {stream => 
        properties.load(stream)
      }      
    }

    properties
  }
}
