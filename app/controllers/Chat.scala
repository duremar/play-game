package controllers
import play.api.mvc._

import ru.tetplay.{Join, ChatRoom}
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.iteratee.{Enumerator, Iteratee}

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import play.libs.Akka

object Chat extends Controller {
  implicit val timeout = Timeout(1.seconds)
  val room:ActorRef = Akka.system.actorOf(Props[ChatRoom])
  def showRoom(nick: String) = Action { implicit request =>
    println("show room for "+nick)
    Ok(views.html.chat.showRoom(nick))
  }
  def chatSocket(nick: String) = WebSocket.async { request =>
    println("chat socket for " +nick)
    val channelsFuture = room ? Join(nick)
    channelsFuture.mapTo[(Iteratee[String, _], Enumerator[String])]
  }
}