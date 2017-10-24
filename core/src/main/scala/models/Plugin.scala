package core.models

import play.api.libs.json._

/**
  * A plug-in class.
  *
  * Requires POST content:
  *   - id: a unique identifier, e.g. com.yourname.yourplugin
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
class Plugin (
  val id: String,
  val name: String,
  val website: String,
  val version: String,
  val description: String,
  val icon: String,
  val base: String,
  val provides: Map[String, String]
)
{
  /**
    * A secondary class constructor, to create from JSON.
    */
  def this(json: JsValue) {
    this(
      json("plugin").as[String],
      json("name").as[String],
      json("website").as[String],
      json("version").as[String],
      json("description").as[String],
      json("icon").as[String],
      json("base").as[String],
      json("provides").as[Map[String,String]]
    )
  }

  /**
    * Realise the plug-in as JSON
    */
  def toJson(): JsValue = {
    Json.obj(
      "plugin" -> id,
      "name" -> name,
      "website" -> website,
      "version" -> version,
      "description" -> description,
      "icon" -> icon,
      "base" -> base,
      "provides" -> Json.toJson(provides)
    )
  }
}
