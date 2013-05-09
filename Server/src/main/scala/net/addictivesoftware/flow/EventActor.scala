package net.addictivesoftware.flow

import akka.actor.Actor
import com.weiglewilczek.slf4s.Logging
import net.addictivesoftware.flow.objects.{WebEvent, WebEventDAO}

case class RecordEvent(event:WebEvent)

class EventActor extends Actor with Logging {
  def receive = {
    case RecordEvent(event) => {
      //logger.info("logging event")
      WebEvent.insert(event)
    }
    case _ => {
      logger.error("Unhandled message sent to this actor")
    }
  }
}
