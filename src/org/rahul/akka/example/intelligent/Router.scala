package org.rahul.akka.example.intelligent

import akka.actor.{Actor, ActorRef}

import scala.collection.immutable.Queue

class Router(producer: ActorRef, worker: ActorRef) extends Actor {
  var upStreamQueue = Queue.empty[Item]
  var downStreamQueue = Queue.empty[ActorRef]

  override def preStart() = {
    super.preStart()
    producer ! Continue
  }

  override def receive: Receive = {

    case Continue =>
      if(upStreamQueue.isEmpty) { // Just keep worker in the registry when there is no work
        downStreamQueue = downStreamQueue.enqueue(sender)
      }
      else {
        val (item, newQueue) = upStreamQueue.dequeue
        upStreamQueue = newQueue

        sender   ! item     // pass-on item to worker
        producer ! Continue // tell producer to create more work
      }
  }
}
