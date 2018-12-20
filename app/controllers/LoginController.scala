package controllers

import akka.stream.Materializer
import javax.inject.Inject
import models.LoginDetails
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}

class LoginController @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def login: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(LoginDetails.loginForm))
  }

  def loginSubmit: Action[AnyContent] = Action { implicit request =>
    LoginDetails.loginForm.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.login(formWithErrors))
    }, { loginDetails =>
      // TODO - process details properly
      if (LoginDetails.checkCredentials(loginDetails)) {
        Redirect(routes.Application.index()).withSession(request.session + ("username" -> loginDetails.username))
      } else {
        Redirect(routes.LoginController.login())
      }
    })
  }

}
