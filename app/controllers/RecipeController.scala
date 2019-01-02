package controllers

import akka.stream.Materializer
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.AnyContent
import services.MongoServices
import reactivemongo.bson.{BSONDocument, BSONObjectID}

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

  def editRecipe(id: String): Action[AnyContent] = Action.async { implicit request =>
    mongoServices.getRecipe(
      request.session.get(Constants.username.toString).getOrElse(Constants.emptyString.toString),
      id
    ).map( recipe =>
      Ok(
        views.html.editRecipe(
          Recipe.recipeForm.fill(
            Recipe(
              Some(recipe.head.id.get),
              recipe.head.title,
              recipe.head.steps,
              request.session.get(Constants.username.toString).getOrElse(Constants.emptyString.toString)
            )
          )
        )
      )
    )
  }

  def editRecipeSubmit: Action[AnyContent] = Action.async { implicit request =>
    Recipe.recipeForm.bindFromRequest.fold(
      { formWithErrors =>
        Future {
          BadRequest(views.html.recipe(formWithErrors))
        }
      }, { recipe =>
        mongoServices.getCollection(Constants.recipes.toString).map(_.findAndUpdate(BSONDocument(Constants.id.toString -> recipe.id.getOrElse(Constants.emptyString.toString)), recipe))
          .map(_ =>
            Redirect(routes.RecipeController.recipe())
          )
      }
    )
  }

}
