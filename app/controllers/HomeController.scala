package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with ControllerHelpers{

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  

  import services.AuthService
  import services.AuthServiceMessages._

  import services.ChatService
  import services.ChatServiceMessages._

  // TODO: Complete:
  //  - Check if the user is logged in
  //     - If they are, create a message from the relevant author
  //     - If they aren't, redirect to the login page
  //
  // NOTE: We don't know how to create HTML yet,
  // so populate each response with a plain text message.
  def submitMessage(text: String) = Action { implicit request:Request[AnyContent] =>
    withAuthenticatedUser(request){cred => 
      ChatService.chat(cred.username,text)
      Redirect(routes.HomeController.index)
    }
  }


  // TODO: Complete:
  //  - Check if the user is logged in
  //     - If they are, return an Ok response containing a list of messages
  //     - If they aren't, redirect to the login page
  //
  // NOTE: We don't know how to create HTML yet,
  // so populate each response with a plain text message.
  

  def index() = Action { implicit request: Request[AnyContent] =>
    withAuthenticatedUser(request){ cred => 
      Ok(ChatService.messages.mkString("\n"))
    }
  }

  def withAuthenticatedUser(request:Request[AnyContent])(func:Credentials => Result):Result = request.sessionCookieId match {
    case Some(sessionId) => AuthService.whoami(sessionId) match {
      case res: Credentials => func(res)
      case res: SessionNotFound => BadRequest("Not Logged In!") 
    } 
    case None => BadRequest("Not Logged In!")
  }
}
