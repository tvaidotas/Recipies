package controllers

import akka.stream.Materializer
import javax.inject.Inject
import models.Login
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def login = Action {
    Ok(views.html.login(Login.loginForm))
  }

  def loginSubmit = Action {
    Ok("Logged in succesfuly")
  }

}
