package models

import play.api.data._
import play.api.data.Forms._

case class LoginDetails(username: String, password: String)

object LoginDetails {

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginDetails.apply)(LoginDetails.unapply)
  )

  def checkCredentials(loginDetails: LoginDetails): Boolean = {
    if (loginDetails.username == "admin" && loginDetails.password == "password")
      true
    else
      false
  }

  def checkUser(loginDetails: String): String = {
    if (loginDetails == "admin")
      "admin"
    else
      ""
  }

}
