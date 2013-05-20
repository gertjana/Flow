package net.addictivesoftware.flow

import spray.routing.directives.ContentTypeResolver
import akka.actor.ActorRefFactory
import spray.routing._
import directives.BasicDirectives._
import directives.ExecutionDirectives._
import directives.MethodDirectives._
import directives.RespondWithDirectives._
import directives.RouteDirectives._
import java.net.{URLConnection, URL}
import shapeless.{HNil, ::}
import spray.httpx.marshalling.BasicMarshallers
import org.parboiled.common.FileUtils
import scala.Some
import spray.http.HttpHeaders.`Last-Modified`
import spray.http.DateTime

trait FlowDirectives {
  def getFromResourceAndReplacePlaceHolders(resourceName: String)
                     (implicit resolver: ContentTypeResolver, refFactory: ActorRefFactory): Route = {

    def openConnection: Option[URL] :: HNil => Directive[URLConnection :: HNil] = {
      case Some(url) :: HNil => provide(url.openConnection())
      case _ => reject
    }

    if (!resourceName.endsWith("/")) {
      def resource = getClass.getClassLoader.getResource(resourceName)
      (get & detachTo(singleRequestServiceActor) & provide(Option(resource)))
        .hflatMap(openConnection) { urlConn =>
        implicit val bufferMarshaller = BasicMarshallers.byteArrayMarshaller(resolver(resourceName))
        respondWithLastModifiedHeader1(urlConn.getLastModified) {
          complete(replaceEnvPlaceHolders(new String(FileUtils.readAllBytes(urlConn.getInputStream))))
        }
      }
    } else reject // don't serve the content of resource "directories"
  }
  def respondWithLastModifiedHeader1(timestamp: Long): Directive0 =
    respondWithHeader(`Last-Modified`(DateTime(math.min(timestamp, System.currentTimeMillis))))


  //replaces placeholders with Env var's
  def replaceEnvPlaceHolders(text: String) = {
    "\\$\\{(\\w+)\\}".r.replaceAllIn(text, m => Option(System.getenv(m.group(1).toString)) getOrElse FlowProperties.getString("application-host"))

  } 

}
