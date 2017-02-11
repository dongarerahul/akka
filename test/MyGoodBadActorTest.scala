import akka.actor.{ActorRef, ActorSystem, Props}
import org.scalatest._
import akka.testkit.TestKit
import akka.pattern.ask
import akka.util.Timeout
import org.rahul.akka.example.simple
import org.rahul.akka.example.simple.{MyBadMutableActor, MyGoodActor}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class MyGoodBadActorTest extends TestKit(ActorSystem("MyGoodBacActorTests"))
  with Matchers
  with FlatSpecLike
  with BeforeAndAfter
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(3 seconds)

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Good Actor" should "provide Store and Retrieve feature" in {
    val goodActorRef: ActorRef = system.actorOf(Props[MyGoodActor], name = "GoodActor")
    goodActorRef ! simple.Add("FirstItem")

    val result = goodActorRef ? simple.Contains("FirstItem")
    result.onComplete {
      case Success(item) => item shouldBe true;
      case Failure(error) => error.printStackTrace(); error shouldBe true
    }

    val result2 = goodActorRef ? simple.Contains("SecondItem")
    result2.onComplete {
      case Success(item) => item shouldBe false;
      case Failure(error) => error.printStackTrace(); error shouldBe true
    }
  }

  "Bad Actor" should "run" in {
    val badActorRef: ActorRef = system.actorOf(Props[MyBadMutableActor], name = "BadActor")
    badActorRef ! simple.Add("FirstItem")

    val result = badActorRef ? simple.Contains("FirstItem")
    result.onComplete {
      case Success(item) => item shouldBe true;
      case Failure(error) => error.printStackTrace(); assert(false)
    }

    val result2 = badActorRef ? simple.Contains("SecondItem")
    result2.onComplete {
      case Success(item) => item shouldBe false;
      case Failure(error) => error.printStackTrace(); error shouldBe false
    }
  }
}