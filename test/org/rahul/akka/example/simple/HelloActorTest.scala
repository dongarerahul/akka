package org.rahul.akka.example.simple

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import org.rahul.akka.example.simple
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class HelloActorTest extends TestKit(ActorSystem("HelloActors"))
  with WordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with Matchers {


  override def afterAll(): Unit = {
    println("Shutting down actors system")
    TestKit.shutdownActorSystem(system)
  }

  "An org.rahul.akka.example.simple.HelloActor using implicit sender " must {
    "send back message 'Hello World' " in {
      val actor = system.actorOf(Props[simple.HelloActor], name = "org.rahul.akka.example.simple.HelloActor")
      actor ! MyMessage("Hello")
      expectMsg(MyMessage("Hello World"))
    }

    "catch response message using future " in {
      implicit val timeout = Timeout(3 seconds)
      val actorRef = TestActorRef(new simple.HelloActor)
      val future = actorRef ? MyMessage("Hello")
      future.onComplete {
        case Success(result) => result should be (MyMessage("Hello World"))
        case Failure(error)  => error.printStackTrace(); assert(false)
      }
    }

    "throw exception when see unhanled message " in {
      val actorRef = TestActorRef(new simple.HelloActor)
      intercept[IllegalArgumentException] {
        actorRef.receive(MyMessage("Bad Message"))
      }
    }

  }
}
