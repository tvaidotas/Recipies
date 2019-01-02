package services

import helpers.Constants
import javax.inject.Inject
import models.{LoginDetails, Recipe, SignUp}
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import helpers.JsonFormats._
import reactivemongo.play.json._

import scala.concurrent.Future

class MongoServices @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  def getCollection(collectionName: String): Future[JSONCollection] = {
    database.map(_.collection[JSONCollection](collectionName))
  }

  def validUser(loginDetails: LoginDetails) = {
    getCollection(Constants.loginDetails.toString).map {
      _.find(
        Json.obj(
          Constants.firstName.toString -> loginDetails.username,
          Constants.lastName.toString -> loginDetails.password
        )
      ).cursor[SignUp]
    }.flatMap(_.collect[List]()).map(list => list.length == 1)
  }

  def getAllRecipes(username: String) = {
    getCollection(Constants.recipes.toString).map {
      _.find(
        Json.obj(
          Constants.username.toString -> username
        )
      )
        .sort(Json.obj(Constants.created.toString -> -1))
        .cursor[Recipe]
    }.flatMap(_.collect[List]())
  }

  def deleteRecipe(id: String) = Action.async { implicit request =>
    getCollection(Constants.recipes.toString)
      .map{_.findAndRemove(Json.obj(Constants.id.toString -> id))}
      .map(_ => Ok("Deleted recipe"))
  }

}
