package org.rahul.akka.example.simple

import java.util.logging.Logger

import akka.actor.Actor

import scala.collection.mutable

class MyBadMutableActor extends Actor {
  val logger = Logger.getLogger("MyBadMutableActor")
  val isInSet = mutable.Set.empty[String]

  override def receive = {
    case Add(key)       => logger.info("MyBadActor -> Add Message Received"); isInSet += key
    case Contains(key)  => logger.info("MyBadActor -> Contains Message Received"); sender() ! isInSet(key)
  }
}
case class Add(key: String)
