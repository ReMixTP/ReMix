package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RemixCore @Inject()(cc: ControllerComponents) extends AbstractController(cc)
    // with RemixReasoners
    // with RemixTranslators
    // with RemixPresenters
    // with RemixState
{

  /**
   * Create an Action to return a test JSON string.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/api/v1/test`.
   */
  def test() = Action { implicit request: Request[AnyContent] =>
    Ok("true")
  }
}
