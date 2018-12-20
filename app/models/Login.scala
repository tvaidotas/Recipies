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

}
