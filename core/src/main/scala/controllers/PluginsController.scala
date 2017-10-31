package core.controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.Files._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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
  def list() = Action.async { implicit request: Request[AnyContent] =>
    Plugins.ids.map { (ids) =>
      Ok(Json.obj(
        "plugins" -> JsArray(ids.map{ n => JsString(n) })
      ))
    }
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
  def about(pluginID: String, feature: String) = Action.async {implicit request: Request[AnyContent] =>
    Plugins.contains(pluginID).flatMap{ (havePlugin) =>
      if (!havePlugin) {
        Future { BadRequest(Json.obj(
          "result" -> "failure",
          "reason" -> ("Plug-in '" + pluginID + "' does not exist.")
        )) }
      } else if (feature == "all") {
        Plugins.get(pluginID).map { (plugin) =>
          Ok(plugin.toJson())
        }
      } else {
        try {
          Plugins.get(pluginID).map { (plugin) =>
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
          }
        } catch {
          case e: MatchError => Future { BadRequest(Json.obj(
            "result" -> "failure",
            "reason" -> ("Unknown plug-in attribute '" + feature + "'"))) }
        }
      }
    }
  }

  /**
    * Get info about all plug-ins!
    */
  def aboutAll(plugin: String) = about(plugin, "all")
}
