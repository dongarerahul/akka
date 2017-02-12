package org.rahul.akka.example.intelligent

import java.util.logging.Logger

import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive

class Worker extends Actor {

  var router : ActorRef = null
  val logger = Logger.getLogger("Worker")

  override def preStart = {
    super.preStart
  }

  def process(item: Item): Unit = println("********* Worker.Process : " + item)

  override def receive: Receive = {

    case item: Item =>
      logger.info(s"********** Worker->Receive :: Got Item Assigned :: $item")
      process(item)
      router ! Continue
    case reference: ActorRef =>
      logger.info("********** Worker->Receive :: Received Router Reference !")
      this.router = reference
      //router ! Continue
  }
}
