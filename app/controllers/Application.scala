package controllers

import akka.stream.Materializer
import javax.inject.Inject
import models.Login
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Application @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}