package services

object ChatServiceMessages {
  case class Message(author: String, text: String)
  case class ChatRequest(text:String)
}
