package net.addictivesoftware.flow

import org.specs2.mutable._
import spray.testkit.Specs2RouteTest
import net.addictivesotware.flow.FlowRoutingService
import spray.http.StatusCodes._
import spray.http.MediaTypes._
import spray.http.HttpCharsets._
import net.addictivesoftware.flow.objects.{EventObject, WebEvent}

//implicit marshallers/unmarshallers
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.flow.MyJsonProtocol._

class FlowServiceSpec extends Specification with Specs2RouteTest with FlowRoutingService {
 	def actorRefFactory = system

  "The Flow Service" should {

    "ensure that the root path is not handled " in {
      Get() ~> flowRoute ~> check {
        handled must beFalse
      }
    }

    "return index.html as text/html" in {
    	Get("/flow/index.html") ~> flowRoute ~> check {
    		status mustEqual OK
    		contentType.charset mustEqual `UTF-8`
    		contentType.mediaType mustEqual `text/html`
    	}
    }

    "return a random number for the session" in {
  		Get("/flow/getsession") ~> flowRoute ~> check {
  			status mustEqual OK
  			entityAs[String].toLong !== 0
  		}
  	}

  	"return /flow/js/flow.js as application/javascript" in {
  		Get("/flow/js/flow.js") ~> flowRoute ~> check {
  			status mustEqual OK
  			contentType.mediaType mustEqual `application/javascript`
  		}
  	}

  	"return /flow/css/main.css as text/css" in {
  		Get("/flow/css/main.css") ~> flowRoute ~> check {
  			status mustEqual OK
  			contentType.mediaType mustEqual `text/css`
  		}
  	}

  	"ensure that an unknown js file is not handled" in {
  		Get("/flow/js/unknown.js") ~> flowRoute ~> check {
  			handled must beFalse
  		}
  	}

  	"posting an event should result in an OK" in {
  		Post("/flow/event/1337/click") ~> flowRoute ~> check {
  			status mustEqual OK
  			entityAs[String] mustEqual "Ok"

  		}
  	}
    
    // api
  	"getting a list for this session id should result in a list of EventObjects" in {
  		Get("/flow/api/events/list/session/1337") ~> flowRoute ~> check {
  			entityAs[List[EventObject]].size !== null
  		}
  	} 

    "api call should return json" in {
      Get("/flow/api/events/list") ~> flowRoute ~> check {
        status mustEqual OK
        contentType.mediaType mustEqual `application/json`
      }
    }
  }
}

