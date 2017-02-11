package org.rahul.akka.example.simple

import java.util.logging.Logger

import akka.actor.Actor

//no mutable state outside actor context
class MyGoodActor extends Actor {

  val logger = Logger.getLogger("org.rahul.akka.example.simple.MyGoodActor")

  override def receive = active(Set.empty)

  def active(isInSet: Set[String]) : Receive = {
    case Add(key)       => logger.info("MyGoodctor -> org.rahul.akka.example.simple.Add Message Received !"); context become active(isInSet + key)
    case Contains(key)  => logger.info("org.rahul.akka.example.simple.MyGoodActor -> org.rahul.akka.example.simple.Contains Message Received !"); sender() ! isInSet(key)
  }
}
