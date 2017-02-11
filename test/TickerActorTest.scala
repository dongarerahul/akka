import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpecLike, Matchers}
import akka.pattern.ask
import akka.util.Timeout

import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TickerActorTest extends TestKit(ActorSystem("TickerActorTest"))
  with Matchers
  with FlatSpecLike
  with BeforeAndAfter
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(3 seconds)

  override def afterAll() = {
      TestKit.shutdownActorSystem(system)
    }

  "Ticker Actor" should "accept tick message to increase counter" in {
    val goodActorRef = system.actorOf(Props[TickerActor], name = "TickerActor")

    goodActorRef ! Tick;
    val future = goodActorRef ? Status;

    future.onComplete {
      case Success(result) => result should be (1)
      case Failure(error)  => error.printStackTrace();assert(false)
    }

  }

}
