package models

import play.api.data._
import play.api.data.Forms._

case class SignUp(firstName: String, lastName: String, username: String, password: String)

object SignUp {

  val signupForm = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(SignUp.apply)(SignUp.unapply)
  )

}