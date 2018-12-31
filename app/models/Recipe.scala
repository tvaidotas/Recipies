package models

import play.api.data._
import play.api.data.Forms._

case class Recipe(title: String, steps: String, username: String)

object Recipe {

  val recipeForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "steps" -> nonEmptyText,
      "username" -> nonEmptyText
    )(Recipe.apply)(Recipe.unapply)
  )

}