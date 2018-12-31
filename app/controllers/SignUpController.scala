package controllers

import akka.stream.Materializer
import helpers.Constants
import javax.inject.Inject
import models.SignUp
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}
import services.MongoServices
import models.JsonFormats._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future


class SignUpController @Inject()
(val messagesApi: MessagesApi, val materializer: Materializer, val mongoServices: MongoServices)
  extends Controller with I18nSupport {

  def signUp: Action[AnyContent] = Action.async { implicit request =>
    Future{Ok(views.html.signUp(SignUp.signupForm))}
  }

  def signUpSubmit = Action.async { implicit request =>
    SignUp.signupForm.bindFromRequest.fold(
      { formWithErrors =>
        Future {
          BadRequest(views.html.signUp(formWithErrors))
        }
      }, { signUpDetails =>
        mongoServices.getCollection("test").flatMap(_.insert(signUpDetails))
          .map(_ =>
            Redirect("/").withSession(request.session + (Constants.username.toString -> signUpDetails.username))
          )
      }
    )
  }

}