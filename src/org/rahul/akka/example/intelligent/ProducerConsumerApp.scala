package org.rahul.akka.example.intelligent

import akka.actor.{ActorSystem, Props}

object ProducerConsumerApp {

  def main(args: Array[String]) {
    val system = ActorSystem("ProducerConsumerSystem")

    val producer = system.actorOf(Props(new Producer(new DataSource())), name = "producer")
    val worker   = system.actorOf(Props[Worker], name = "worker")
    val router = system.actorOf(Props(new Router(producer, worker)), name = "router")

    worker ! router
    producer ! router
  }
}
