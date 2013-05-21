package net.addictivesoftware.flow

import java.util.Properties
import java.io.IOException


object FlowProperties {
  val propFilename = "/flow.properties"

  def getString(name:String): String = {
    flowProps.getProperty(name)
  }

  def getInt(name:String): Int = {
    flowProps.getProperty(name).toInt
  }

  def getEnvOrProp(name: String) : String = {
    Option(System.getenv(name)) getOrElse getString(name)
  }


  protected lazy val flowProps: java.util.Properties = {
    val props = new java.util.Properties
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
