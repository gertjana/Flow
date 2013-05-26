package net.addictivesoftware.flow

import java.util.Properties
import java.io.IOException
import com.weiglewilczek.slf4s.Logging


object FlowProperties extends Logging {
  var propFilename = "/flow.properties"
  val hostName = Option(java.net.InetAddress.getLocalHost().getHostName)


  def getString(name:String): String = {
    flowProps.getProperty(name)
  }

  def getInt(name:String): Int = {
    flowProps.getProperty(name).toInt
  }

  def getEnvOrProp(name: String) : String = {
    Option(System.getenv(name)) match {
      case Some(value) =>
        value
      case _ =>
        getString(name)
    }
  }


  protected lazy val flowProps: java.util.Properties = {
    val props = new java.util.Properties

    hostName match {
      case Some(name) => propFilename = "/flow-" + name + ".properties"
      case _ => {}
    }

    val stream = getClass.getResourceAsStream(propFilename)
    if (stream ne null)
      quietlyDispose(props.load(stream), stream.close)

    props
  }

  private def quietlyDispose(action: => Unit, disposal: => Unit) =
    try     { action }
    finally {
      try     { disposal }
      catch   { case _: IOException => }
    }

}
