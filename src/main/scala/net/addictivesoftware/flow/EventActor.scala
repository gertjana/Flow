package net.addictivesoftware.flow

import akka.actor.Actor
//import com.weiglewilczek.slf4s.Logging
import net.addictivesoftware.flow.objects.{EventObject, WebEvent}

sealed trait EventMessages
case class RecordEvent(session:String, event:String, data:Map[String, String]) extends EventMessages

class EventActor extends Actor { //with Logging {
  def receive = {
    case RecordEvent(session, event, data) => {
      WebEvent.insert(new EventObject(session=session, event=event,data=data))
    }
    case _ => {
 //     logger.error("Unhandled message sent to this actor")
    }
  }
}

