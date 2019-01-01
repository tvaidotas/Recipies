package helpers

import models.{LoginDetails, Recipe, SignUp}
import play.api.libs.json._

object JsonFormats {

  implicit val feedFormat: Format[SignUp] = Json.format[SignUp]
  implicit val userFormat: Format[LoginDetails] = Json.format[LoginDetails]
  implicit val recipeFormat: Format[Recipe] = Json.format[Recipe]

}
