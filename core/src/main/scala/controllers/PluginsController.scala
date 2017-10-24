package core.controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.Files._

import core.models._

/**
  * This controller handles the plug-in maintenance of ReMix
  */
@Singleton
class PluginsController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
{
  /**
    * Return a list of available plug-ins.
    */
  def list() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj(
      "plugins" -> JsArray(Plugins.ids.map{ n => JsString(n) })
    ))
  }

  /**
    * Register a plug-in
    *
    * Requires JSON POST content:
    *   - plugin: a unique identifier, e.g. com.yourname.yourplugin
    *   - name: a human-readable name for your plug-in
    *   - website: a website where users can find out more
    *   - version: which version of your plug-in we are using
    *   - description: a short human-readable description of your plug-in
    *   - icon: a URL for your icon
    *   - base: a base URL for all REST accesses (e.g. http://yoursite.com)
    *   - provides: a map from "interface" keys to REST access points
    *     (e.g. "language" -> "/api/v1/language")
    *
    * The possible "interface" keys in the map are:
    *   - language: to query languages that your plug-in can accept and produce
    *   - inference: to list and apply inference rules
    *   - render: to create a valid HTML snippet to render something
    *   - translate: a translator to produce new languages
    * Your plug-in can implement as many or as few interfaces as necessary.
    */
  def register() = Action { implicit request: Request[AnyContent] =>
    val pluginJson = request.body.asJson.get
    val pluginID = pluginJson("plugin").as[String]
    if (Plugins.contains(pluginID)) {
      BadRequest(Json.obj(
        "result" -> "failure",
        "reason" -> ("Plug-in with ID '" + pluginID + "' already exists")
      ))
    } else {
      Plugins.add(pluginID, new Plugin(pluginJson))
      Ok(Json.obj("result" -> "success"))
    }
  }

  def remove(plugin: String) = Action { implicit request: Request[AnyContent] =>
    Plugins.remove(plugin)
    Ok(Json.obj(
      "result" -> "success"
    ))
  }

  /**
    * Get full details about a plug-in
    *
    * This API access point returns a JSON object with all the information about
    * the plug-in, including the ID (as "plugin"), name, version, description,
    * website, icon, base URL, and the services it provides.
    * if `feature` is "all", then all are given back, else just "plugin" and the
    * appropriate key are given back.
    */
  def about(pluginID: String, feature: String) = Action {implicit request: Request[AnyContent] =>
    if (!Plugins.contains(pluginID)) {
      BadRequest(Json.obj(
        "result" -> "failure",
        "reason" -> ("Plug-in '" + pluginID + "' does not exist.")
      ))
    } else if (feature == "all") {
      Ok(Plugins.get(pluginID).toJson())
    } else {
      try {
        val plugin = Plugins.get(pluginID)
        val featVal = feature match {
          case "plugin"      => Json.toJson(plugin.id)
          case "name"        => Json.toJson(plugin.name)
          case "website"     => Json.toJson(plugin.website)
          case "version"     => Json.toJson(plugin.version)
          case "description" => Json.toJson(plugin.description)
          case "icon"        => Json.toJson(plugin.icon)
          case "base"        => Json.toJson(plugin.base)
          case "provides"    => Json.toJson(plugin.provides)
        }
        Ok(Json.obj(
          "plugin" -> pluginID,
          feature -> featVal
        ))
      } catch {
        case e => BadRequest(Json.obj(
          "result" -> "failure",
          "reason" -> ("Unknown plug-in attribute '" + feature + "'")))
      }
    }
  }

  def aboutAll(plugin: String) = about(plugin, "all")
}
