import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class ActorNotLeakingStateTest extends TestKit(ActorSystem("ActorNotLeakingTest"))
  with WordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with Matchers {

  implicit val timeout = Timeout(300000 seconds)

  override def afterAll(): Unit = {
    println("afterAll : Shutting down actors system")
    Thread.sleep(10000)
    TestKit.shutdownActorSystem(system)
  }

  "ActorNotLeakingState1 should process messages for storeing and retrieving state" in {
    val actor = system.actorOf(Props[ActorNotLeakingState1], name = "ActorNotLeaking1")

    val result = actor ? Add("Hello1")
    result onComplete {
      case Success(Continue) => "Added Successfully !"
      case Success(Reject)   => "Rejected Successfully !"
      case Failure(error)    => "Failure"
    }

    actor ! Add("Hello2")

    checkResult(actor, "Hello1")
    checkResult(actor, "Hello2")
  }

  def checkResult(actor: ActorRef, key: String, attempt: Int = 1) = {
    val result = actor ? IsPresent(key)
    result onComplete {
      case Success(isPresent) => {
        if(isPresent==false) {
          println(s"Still Not Added $key ... Trying $attempt attempt")
          checkResult(actor, key, attempt+1)
        }
        isPresent shouldBe(true)
      }
      case Failure(error) => error.printStackTrace();assert(false);
    }
  }
}
