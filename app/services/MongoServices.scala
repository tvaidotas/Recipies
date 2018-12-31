package services

import helpers.Constants
import javax.inject.Inject
import models.{LoginDetails, SignUp}
import play.api.mvc.Controller
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import models.JsonFormats._
import reactivemongo.play.json._

import scala.concurrent.Future

class MongoServices @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  def getCollection(collectionName: String): Future[JSONCollection] = {
    database.map(_.collection[JSONCollection](collectionName))
  }

  def validUser(loginDetails: LoginDetails) = {
    getCollection("test").map {
      _.find(
        Json.obj(
          Constants.firstName.toString -> loginDetails.username,
          Constants.lastname.toString -> loginDetails.password
        )
      ).cursor[SignUp]
    }.flatMap(_.collect[List]()).map(list => list.length == 1)
  }

}
