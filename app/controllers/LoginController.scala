package controllers

import akka.stream.Materializer
import helpers.Constants
import models.LoginDetails
import play.api.i18n.{I18nSupport, MessagesApi}
import services.MongoServices
import javax.inject.Inject

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class LoginController @Inject()
(val messagesApi: MessagesApi, val materializer: Materializer, val mongoServices: MongoServices) extends Controller
  with I18nSupport {

  def login: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(LoginDetails.loginForm))
  }

  def loginSubmit: Action[AnyContent] = Action.async { implicit request =>
    var loginDetails = LoginDetails("", "")
    LoginDetails.loginForm.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.login(formWithErrors))
    }, { details =>
      loginDetails = details
    })
    mongoServices.validUser(loginDetails).map (result => {
      if (result) {
        Redirect(routes.Application.index())
          .withSession(request.session + (Constants.username.toString -> loginDetails.username))
      } else {
        Redirect(routes.LoginController.login())
      }
    })
  }

}
