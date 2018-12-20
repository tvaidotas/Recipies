package models

import play.api.data._
import play.api.data.Forms._

case class Login(username: String, password: String)

object Login {

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  def checkCredentials(loginDetails: Login): Boolean = {
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
