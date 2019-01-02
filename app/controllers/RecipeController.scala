package controllers

import akka.stream.Materializer
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.AnyContent
import services.MongoServices
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Future
import helpers.Constants
import javax.inject.Inject
import models.Recipe
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import helpers.JsonFormats._
import reactivemongo.play.json._

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

  def deleteRecipe(id: String): Action[AnyContent] = Action.async { implicit request =>
    mongoServices
      .getCollection(Constants.recipes.toString)
        .map{
          _.findAndRemove(
            Json.obj(
              Constants.id.toString -> id,
              Constants.username.toString -> request.session.get(Constants.username.toString).get
            )
          )
        }
        .map(_ => Redirect(routes.Application.index()))
  }

}
