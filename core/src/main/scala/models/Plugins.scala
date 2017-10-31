package core.models

import javax.inject.Inject

import java.io._
import scala.io._
import scala.util.{Success, Failure}
import scala.language.postfixOps
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import play.api.http.HttpEntity

import core.clients._

/**
  * Plugins: a collection of Plugin objects.
  *
  * This singleton is designed to manage all the registered plug-ins that ReMix
  * knows about. It enables and disables them, and is able to return various
  * subsets of the plug-ins depending on what need there is.
  */
object Plugins
{
  val pluginsSources: Set[String] = loadListOfPluginSources()
  val pluginsByID: Future[Map[String, Plugin]] = loadAllPlugins

  /**
    * Read a list of URLs known to provide plug-ins
    *
    * For now, this just reads a file called "plugin-sources.cfg" from the conf
    * directory, but eventually this will come from a central repository of
    * plug-ins (hopefully!). All it does is just read the file and return a set;
    * the more sophisticated loading will be done on request.
    */
  private def loadListOfPluginSources(): Set[String] = {
    // Start with a config file for now, make it more impressive later
    val configFile = Source.fromFile("/etc/remix.cfg")
    return configFile.getLines.toSet
  }

  /**
    *  Load every plug-in in the configuration
    */
  def loadAllPlugins() : Future[Map[String,Plugin]] = {
    val emptyMap : Future[Map[String,Plugin]] = Future{Map()}
    val f: Future[Map[String,Plugin]] = pluginsSources.foldLeft (emptyMap) ({
      (pluginMapFuture: Future[Map[String,Plugin]], url: String) => { // String -> Future[Map[String, Plugin]]
        val newMapExtensionFuture = loadPluginFromSource(url) map {
           (ps: Seq[Plugin]) => { // Seq[Plugin] -> Map[String, Plugin]
             (ps map {
               (p: Plugin) => (p.id -> p) // Plugin -> (String * Plugin)
             }).toMap
           }
         }
         for {
           currentMap <- pluginMapFuture
           extensionMap <- newMapExtensionFuture
         } yield (currentMap ++ extensionMap)
      }
    })
    f onComplete {
      case Success(m) => m
      case Failure(t) => println(t.getMessage)
    }
    return f
  }

  /**
    * Create all the plug-ins available from the given URL
    *
    * Requires a URL that will return list of JSON objects containing:
    *   - plugin: a unique identifier, e.g. com.yourname.yourplugin
    *   - name: a human-readable name for your plug-in
    *   - website: a website where users can find out more
    *   - version: which version of your plug-in we are using
    *   - description: a short human-readable description of your plug-in
    *   - icon: a URL for your icon
    *   - base: a base URL for all REST accesses (e.g. http://yoursite.com)
    *   - provides: a map (object) from "interface" keys to REST access points
    *     (e.g. "language" -> "/api/v1/language")
    *
    * The possible "interface" keys in the map are:
    *   - language: to query languages that your plug-in can accept and produce
    *   - inference: to list and apply inference rules to proof state
    *   - render: to create a valid HTML snippet to render something
    *   - translate: a translator to produce new languages
    *   - engine: able to apply rules to proof state and produce new proof state as a result
    * Your plug-ins can implement as many or as few interfaces as necessary.
    */
  def loadPluginFromSource(url: String): Future[Seq[Plugin]] = {
    val pluginFuture = PluginClient.getJSONFromURL(url)
    return pluginFuture map {
      (pluginJson) => {
        pluginJson.as[Array[JsValue]].map {
          pluginJS => new Plugin(pluginJS)
        }
      }
    }
  }

  /**
    * Return a sequence of plug-in IDs
    */
  def ids() : Future[Seq[String]] = pluginsByID.map{ (m) => m.keys.toSeq}

  /**
    * Determine if the plug-in with the given ID is known.
    */
  def contains(pluginID: String): Future[Boolean] = pluginsByID.map { (m) => m.contains(pluginID) }

  /**
    * Get a plug-in by ID
    */
  def get(pluginID: String): Future[Plugin] = pluginsByID.map { (m) => m(pluginID)}

  def toJsonF(): Future[JsValue] = pluginsByID.map { (m) => JsArray(m.map{
    case (k, v) => v.toJson()
  }.toSeq) }
}
