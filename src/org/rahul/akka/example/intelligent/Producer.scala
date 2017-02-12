package org.rahul.akka.example.intelligent

import java.util.logging.Logger

import akka.actor.{Actor, ActorRef}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class Producer(source: DataSource) extends Actor {

  override def receive: Receive = standBy

  var router : ActorRef = null
  val logger = Logger.getLogger("Producer")

  def standBy: Receive = {
    case PollTick =>

    case reference: ActorRef =>
      logger.info("********** Producer->standBy :: Received Router Reference !")
      this.router = reference
      context.become(polling)

    case Continue =>
      source.next() match {
        case None       => {
          context.become(polling)
          logger.info("*********** Producer Context became polling !")
        } // no data available in the data-source
        case Some(item) => router ! item           // data available in the data-source
      }
  }

  def polling: Receive = {
    case PollTick => {
      logger.info("************* Producer-> Received PollTick Message !")
      source.next() match {
        case None => // Do nothing is no data is available after poll
          logger.info("********** Producer -> polling -> Source have None Item !")
        case Some(item) => { //found some data after poll, then process data and change context to active
          logger.info(s"********** Producer -> polling -> Source have Some Item :: $item")
          router ! item
          context.become(standBy)
        }
      }
    }
  }

  override def preStart() = { // poll after every second
    super.preStart()
    context.system.scheduler.schedule(2.second, 3.second, self, PollTick)
  }
}
