package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with ControllerHelpers with play.api.i18n.I18nSupport{

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
  //  - Create a form for a ChatRequest (defined above)
 val chatForm: Form[ChatRequest] = 
  Form(mapping(
    "text"-> nonEmptyText
  )(ChatRequest.apply)(ChatRequest.unapply)) 


  // TODO: Complete:
  //  - Create a chat room template that accepts the following parameters:
  //     - A list of Messages
  //     - A chat form
  //  - Implement the controller below:
  //     - Check the user is logged in
  //        - If they are, display a web page containing the current messages
  //        - If they aren't, redirect to the login page
 // def index = Action { implicit request =>
  //  ???
 // }

  // TODO: Complete:
  //  - Check the user is logged in
  //     - If they are:
  //        - Parse the form data using the login form
  //           - If it's valid, call ChatService.chat and redirect to the chat room page
  //           - If it's invalid, display an appropriate error message
  //     - If they aren't, redirect to the login page
  def submitMessage = Action { implicit request =>
    withAuthenticatedUser(request){ creds =>
      chatForm.bindFromRequest().fold(
      hasErrors = { form:Form[ChatRequest] => 
        Ok(views.html.chatroom(ChatService.messages,form))
      },
      success = { chatReq: ChatRequest => 
        ChatService.chat(creds.username,chatReq.text)
        Ok(views.html.chatroom(ChatService.messages,chatForm))
      }
    )

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
      Ok(views.html.chatroom(ChatService.messages,chatForm))
    }
  }

  def withAuthenticatedUser(request:Request[AnyContent])(func:Credentials => Result):Result = request.sessionCookieId match {
    case Some(sessionId) => AuthService.whoami(sessionId) match {
      case res: Credentials => func(res)
      case res: SessionNotFound => redirectToLogin 
    } 
    case None => redirectToLogin
  }

  def redirectToLogin:Result = Redirect(routes.AuthController.login)
}
