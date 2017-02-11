package org.rahul.akka.example.simple

import java.util.logging.Logger

import akka.actor.Actor

class HelloActor extends Actor {

  val logger = Logger.getLogger("HelloActor")

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
