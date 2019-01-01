package controllers

import authentication.AuthenticatedAction
import akka.stream.Materializer
import helpers.Constants
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import services.MongoServices
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application @Inject()
(val messagesApi: MessagesApi, val materializer: Materializer, val mongoServices: MongoServices) extends Controller
  with I18nSupport {

  def index = AuthenticatedAction.async { implicit request =>
    mongoServices.getAllRecipes(request.session.get(Constants.username.toString).getOrElse("")).map(recipes =>
      Ok(views.html.index(recipes))
    )
  }

}