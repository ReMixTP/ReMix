package core.models

import scala.io._
import java.io._

import play.api.libs.json._

/**
  * Plugins: a collection of Plugin objects.
  *
  * This singleton is designed to manage all the registered plug-ins that ReMix
  * knows about. It enables and disables them, and is able to return various
  * subsets of the plug-ins depending on what need there is.
  */
object Plugins
{
  var pluginsByID: Map[String,Plugin] = loadConfiguration()

  private def loadConfiguration(): Map[String,Plugin] = {
    val configFile = Source.fromFile(s"${sys.props("user.home")}/.remix").mkString
    val json = Json.parse(configFile)
    return json.as[List[JsValue]].map{
      jsv => jsv("plugin").as[String] -> new Plugin(jsv)
    }.toMap
  }

  def dumpConfiguration() = {
    val json = toJson
    val configFile = new FileWriter( new File(s"${sys.props("user.home")}/.remix") )
    configFile.write(Json.prettyPrint(json))
    configFile.close
  }

  def ids() : Seq[String] = pluginsByID.keys.toSeq

  def contains(pluginID: String): Boolean = pluginsByID.contains(pluginID)

  def get(pluginID: String): Plugin = pluginsByID(pluginID)

  def add(pluginID: String, plugin: Plugin) = {
    pluginsByID += (pluginID -> plugin)
    dumpConfiguration
  }

  def remove(pluginID: String) = {
    pluginsByID -= pluginID
    dumpConfiguration
  }

  def toJson() = JsArray(pluginsByID.map{
    case (k, v) => v.toJson
  }.toSeq)
}
