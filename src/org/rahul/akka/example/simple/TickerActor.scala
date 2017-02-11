package org.rahul.akka.example.simple

import java.util.logging.Logger

import akka.actor.Actor

// Not a goo example where actor is mutating its state
class TickerActor extends Actor {

  val logger = Logger.getLogger("TickerActor")
  var counter = 0

  override def receive: Receive = {
    case Tick   => logger.info("Received Tick Message !");   counter = counter + 1
    case Status => logger.info("Received Status Message !"); sender() ! counter
  }
}

case class Contains(key: String)
case object Tick
case object Status

