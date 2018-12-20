package controllers

import akka.stream.Materializer
import javax.inject.Inject
import models.SignUp
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}


class SignupController @Inject()(val messagesApi: MessagesApi, val materializer: Materializer) extends Controller
  with I18nSupport {

  def signup: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.signup(SignUp.signupForm))
  }

  def signupSubmit: Action[AnyContent] = Action { implicit request =>
    SignUp.signupForm.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.signup(formWithErrors))
    }, { signupDetails =>
      // TODO - process details
      Redirect("/")
    })
  }

}
