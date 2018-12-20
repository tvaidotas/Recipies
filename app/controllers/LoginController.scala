package controllers

import akka.stream.Materializer
import javax.inject.Inject
import models.Login
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}

class LoginController @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def login: Action[AnyContent] = Action { implicit request =>
    println(request.session.get("username"))
    Ok(views.html.login(Login.loginForm))
  }

  def loginSubmit: Action[AnyContent] = Action { implicit request =>
    Login.loginForm.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.login(formWithErrors))
    }, { loginDetails =>
      if (Login.checkCredentials(loginDetails)) {
        println("credentials passed")
        Redirect(routes.Application.index()).withSession(request.session + ("username" -> loginDetails.username))
      } else {
        println("credentials failed")
        Redirect(routes.LoginController.login())
      }
    })
  }

}
