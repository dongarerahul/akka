package org.rahul.akka.example.simple

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class TickerActorTest extends TestKit(ActorSystem("org.rahul.akka.example.simple.TickerActorTest"))
  with Matchers
  with FlatSpecLike
  with BeforeAndAfter
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(3 seconds)

  override def afterAll() = {
      TestKit.shutdownActorSystem(system)
    }

  "Ticker Actor" should "accept tick message to increase counter" in {
    val goodActorRef = system.actorOf(Props[TickerActor], name = "org.rahul.akka.example.simple.TickerActor")

    goodActorRef ! Tick;
    val future = goodActorRef ? Status;

    future.onComplete {
      case Success(result) => result should be (1)
      case Failure(error)  => error.printStackTrace();assert(false)
    }

  }

}
