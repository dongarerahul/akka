package org.rahul.akka.example.intelligent

import java.util.logging.Logger

import akka.actor.{Actor, ActorRef}

import scala.collection.immutable.Queue

class Router(producer: ActorRef, worker: ActorRef) extends Actor {
  var upStreamQueue = Queue.empty[Item]
  var downStreamQueue = Queue.empty[ActorRef]
  val logger = Logger.getLogger("Router")

  override def preStart() = {
    super.preStart()
    producer ! Continue
  }

  override def receive: Receive = {

    case Continue =>
      if (upStreamQueue.isEmpty) {
        // Just keep worker in the registry when there is no work
        downStreamQueue = downStreamQueue.enqueue(sender)
        logger.info("*********** Enqueued worker")
      }
      else {
        val (item, newQueue) = upStreamQueue.dequeue
        upStreamQueue = newQueue

        sender ! item // pass-on item to worker
        producer ! Continue // tell producer to create more work
      }

    case item: Item =>
      if(downStreamQueue.isEmpty) { // if no worker is available in the downStreamQueue
        upStreamQueue.enqueue(item)
      }
      else { // if worker is available, pass on work to it
        val (worker, newQueue) = downStreamQueue.dequeue
        downStreamQueue = newQueue
        worker ! item
        producer ! Continue
      }
  }
}
