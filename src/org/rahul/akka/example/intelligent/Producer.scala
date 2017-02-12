package org.rahul.akka.example.intelligent

import akka.actor.{Actor, ActorRef}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class Producer(source: DataSource) extends Actor {

  override def receive: Receive = standBy

  var router : ActorRef = null

  def standBy: Receive = {
    case PollTick =>

    case reference: ActorRef =>
      this.router = reference
      context.become(polling)

    case Continue =>
      source.next() match {
        case None       => context.become(polling) // no data available in the data-source
        case Some(item) => router ! item           // data available in the data-source
      }
  }

  def polling: Receive = {
    case PollTick => {
      source.next() match {
        case None => // Do nothing is no data is available after poll
        case Some(item) => { //found some data after poll, then process data and change context to active
          router ! item
          context.become(standBy)
        }
      }
    }
  }

  override def preStart() = { // poll after every second
    super.preStart()
    context.system.scheduler.schedule(1.second, 1.second, self, PollTick)
  }
}
