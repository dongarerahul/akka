import akka.actor.Actor

import scala.collection.mutable
import scala.concurrent.Future
import akka.pattern._
import org.rahul.akka.example.advance

import scala.concurrent.ExecutionContext.Implicits.global

// Fix is separate mutable code and keet it small and in separate method
// For bad good, see org.rahul.akka.example.advance.ActorLeakingState.scala

class ActorNotLeakingState extends Actor {

  val isIntSet = mutable.Set.empty[String] // see org.rahul.akka.example.advance.ActorNotLeakingState1 to remove mutable

  def validate(key: String) : Future[Boolean] = Future {
    if(key.contains(" ")) false else true // dummy implementation
  }

  override def receive: Receive = {
    case Add(key) =>
      val list = for (shouldAdd <- validate(key)) yield advance.Validated(key, shouldAdd)
      list.pipeTo(self)

    case Validated(key, isValid) =>
      if(isValid) isIntSet += key
  }
}
