package models

import helpers.Constants
import play.api.data._
import play.api.data.Forms._

case class SignUp(firstName: String, lastName: String, username: String, password: String)

object SignUp {

  val signUpForm = Form(
    mapping(
      Constants.firstName.toString -> nonEmptyText,
      Constants.lastName.toString -> nonEmptyText,
      Constants.username.toString -> nonEmptyText,
      Constants.password.toString -> nonEmptyText
    )(SignUp.apply)(SignUp.unapply)
  )

}
