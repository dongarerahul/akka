package org.rahul.akka.example.intelligent

import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive

class Worker extends Actor {

  var router : ActorRef = null

  override def preStart = {
    super.preStart
  }

  def process(item: Item): Unit = println("Processing : " + item)

  override def receive: Receive = {
    case item: Item =>
      process(item)
      router ! Continue
    case reference: ActorRef =>
      this.router = reference
      router ! Continue
  }
}
