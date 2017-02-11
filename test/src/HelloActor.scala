import java.util.logging.Logger

import akka.actor.Actor
import akka.actor.Actor.Receive

class HelloActor extends Actor {

  val logger = Logger.getLogger("org.rahul.akka.example.simple.HelloActor")

  override def receive: Receive = {
    case MyMessage("Hello")  => {
      logger.info("Received Hello Message")
      sender() ! MyMessage("Hello World")
    }

    case _ => {
      logger.info("Received bad message ! ")
      throw new IllegalArgumentException("Bad Argument !")
    }
  }
}

sealed case class MyMessage(message: String)