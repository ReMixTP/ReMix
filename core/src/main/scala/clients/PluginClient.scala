package core.clients

import javax.inject.Inject

import java.io._
import scala.io._
import scala.concurrent.Future
import scala.concurrent.duration._

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import play.api.http.HttpEntity
import play.api.libs.ws.ahc.AhcWSClient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * PluginClient: handles all external requests related to plug-ins
  *
  * This class is designed to manage all the registered plug-ins that ReMix
  * knows about. It enables and disables them, and is able to return various
  * subsets of the plug-ins depending on what need there is.
  */
object PluginClient
{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val ws = AhcWSClient()

  /**
    * Call the given URL to get JSON information about plug-in
    */
  def getJSONFromURL(url: String): Future[JsValue] = {
    val response: Future[WSResponse] = ws
      .url(url)
      .withHttpHeaders("Accept" -> "application/json")
      .withRequestTimeout(5.seconds)
      .get()
    return response.map {
      r => {r.json}
    }
  }
}
