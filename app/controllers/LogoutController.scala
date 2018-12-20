package controllers

import akka.stream.Materializer
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}

class LogoutController @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def logout: Action[AnyContent] = Action{ implicit request =>
    Ok("You're succesfully logged out").withSession()
  }

}
