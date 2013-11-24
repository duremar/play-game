package ru.tetplay

import play.api.libs.iteratee.{Enumerator, Iteratee, Concurrent}
import akka.actor.Actor
import scala.concurrent.{ExecutionContext, Future}


case class Join(nick: String)
case class Leave(nick: String)
case class Broadcast(message: String)

class ChatRoom extends Actor {
  import ExecutionContext.Implicits.global
  var users = Set[String]()

  val (enumerator, channel) = Concurrent.broadcast[String]
  def receive = {
    case Join(nick) => {
      println("join " + nick+" "+users.size+" "+this)
      if(!users.contains(nick)) {

        val iteratee = Iteratee.foreach[String]{ message =>
          self ! Broadcast("%s: %s" format (nick, message))
        }.mapDone { _ =>
          self ! Leave(nick)
        }
        users += nick
        channel.push("User %s has joined the room, now %s users"
          format(nick, users.size))
        sender ! (iteratee, enumerator)
      } else {
        val enumerator = Enumerator(
          "Nickname %s is already in use." format nick)
        val iteratee = Iteratee.ignore
        sender ! (iteratee, enumerator)
      }
    }
    case Leave(nick) => {
      println("Leave" + nick)
      users -= nick
      channel.push("User %s has left the room, %s users left"
        format(nick, users.size))
    }
    case Broadcast(msg: String) => {
      println("Broadcast " + msg)
      channel.push(msg)
    }
  }
}