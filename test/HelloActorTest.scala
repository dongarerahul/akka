import akka.actor
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout

import scala.util.{Failure, Success}
import scala.concurrent.duration._

class HelloActorTest extends TestKit(ActorSystem("HelloActors"))
  with WordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with Matchers {


  override def afterAll(): Unit = {
    println("Shutting down actors system")
    TestKit.shutdownActorSystem(system)
  }

  "An HelloActor using implicit sender " must {
    "send back message 'Hello World' " in {
      val actor = system.actorOf(Props[HelloActor], name = "HelloActor")
      actor ! MyMessage("Hello")
      expectMsg(MyMessage("Hello World"))
    }

    "catch response message using future " in {
      implicit val timeout = Timeout(3 seconds)
      val actorRef = TestActorRef(new HelloActor)
      val future = actorRef ? MyMessage("Hello")
      future.onComplete {
        case Success(result) => result should be (MyMessage("Hello World"))
        case Failure(error)  => error.printStackTrace(); assert(false)
      }
    }

    "throw exception when see unhanled message " in {
      val actorRef = TestActorRef(new HelloActor)
      intercept[IllegalArgumentException] {
        actorRef.receive(MyMessage("Bad Message"))
      }
    }

  }
}
