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
    downStreamQueue = downStreamQueue.enqueue(worker)
    producer ! Continue
  }

  override def receive: Receive = {

    case Continue =>
      if (upStreamQueue.isEmpty) {
        // Just keep worker in the registry when there is no work
        downStreamQueue = downStreamQueue.enqueue(sender)
        logger.info("***********UpstreamQueue is Empty. Hence Enqueued worker in the downStreamQueue !")
      }
      else {
        val (item, newQueue) = upStreamQueue.dequeue
        upStreamQueue = newQueue
        logger.info("*********** Router.Continue :: Loading item from upStreamQueue and sending it the worker!")
        sender ! item // pass-on item to worker
        producer ! Continue // tell producer to create more work
      }

    case item: Item =>
      logger.info(s"********** Router.Item :: Received $item")
      if(downStreamQueue.isEmpty) { // if no worker is available in the downStreamQueue
        upStreamQueue = upStreamQueue.enqueue(item) // keep item in the upstream queue else assign item to worker
        logger.info("*********** Router.Item :: Enqueing item in the upStreamQueue as no worker is available immediately !")
      }
      else { // if worker is available, pass on work to it
        val (worker, newQueue) = downStreamQueue.dequeue
        downStreamQueue = newQueue
        logger.info(s"*********** Router.Item :: Sending $item to the worker !")
        worker ! item
        producer ! Continue
      }
  }
}
