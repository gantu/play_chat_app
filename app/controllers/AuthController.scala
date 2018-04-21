package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import javax.inject._


@Singleton
class AuthController @Inject()(cc:ControllerComponents) extends AbstractController(cc) with ControllerHelpers{
	import services.AuthService
	import services.AuthServiceMessages._

  // TODO: Complete:
  //  - Call AuthService.login
  //     - If it's LoginSuccess, return an Ok response that sets a cookie
  //     - If it's UserNotFound or PasswordIncorrect, return a BadRequest response
  //
  // NOTE: We don't know how to create HTML yet,
  // so populate each response with a plain text message.
	  def login(username: Username, password: Password) = Action { request =>
	    AuthService.login(LoginRequest(username,password)) match {
	    	case res:LoginSuccess => Ok("successfull login").withSessionCookie(res.sessionId)
	    	case res:UserNotFound  => BadRequest("User not found or password incorrect!")
	    	case res:PasswordIncorrect => BadRequest("User not found or password incorrect!")

	    }
	  }


}