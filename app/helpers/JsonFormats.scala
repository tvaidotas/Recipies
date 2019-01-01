package helpers

import models.{LoginDetails, Recipe, SignUp}
import play.api.libs.json._

object JsonFormats {

  implicit val feedFormat = Json.format[SignUp]
  implicit val userFormat = Json.format[LoginDetails]
  implicit val recipeFormat = Json.format[Recipe]

}
