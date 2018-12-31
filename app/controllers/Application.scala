package controllers

import akka.stream.Materializer
import authentication.AuthenticatedAction
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class Application @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def index = AuthenticatedAction.async { implicit request =>
    Future{Ok(views.html.index("Your new application is ready."))}
  }

}