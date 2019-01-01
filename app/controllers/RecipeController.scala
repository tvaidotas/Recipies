package controllers

import models.Recipe
import akka.stream.Materializer
import helpers.Constants
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}
import services.MongoServices
import helpers.JsonFormats._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

class RecipeController @Inject()
(val messagesApi: MessagesApi, val materializer: Materializer, val mongoServices: MongoServices) extends Controller
  with I18nSupport {

  def recipe: Action[AnyContent] = Action.async { implicit request =>
    Future{
      Ok(
        views.html.recipe(
          Recipe.recipeForm.fill(
            Recipe(
              Some(BSONObjectID.generate().stringify),
              Constants.emptyString.toString,
              Constants.emptyString.toString,
              request.session.get(Constants.username.toString).getOrElse(Constants.emptyString.toString)
            )
          )
        )
      )
    }
  }

  def recipeSubmit: Action[AnyContent] = Action.async { implicit request =>
    Recipe.recipeForm.bindFromRequest.fold(
      { formWithErrors =>
        Future {
          BadRequest(views.html.recipe(formWithErrors))
        }
      }, { recipe =>
        mongoServices.getCollection(Constants.recipes.toString).flatMap(_.insert(recipe))
          .map(_ =>
            Redirect(routes.RecipeController.recipe())
          )
      }
    )
  }

}
