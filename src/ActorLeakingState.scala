import akka.actor.Actor
import akka.actor.Actor.Receive
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// Bad Design where functions that execute asynchronously and t
// hat capture variables that aren't meant to escape their context

class ActorLeakingState extends Actor {

  val isIntSet = mutable.Set.empty[String]

  def validate(key: String) : Future[Boolean] = Future {
    if(key.contains(" ")) false else true // dummy implementation
  }

  override def receive: Receive = {
    case Add(key) =>
      for(shouldAdd <- validate(key)) {
        if(shouldAdd) isIntSet += key // mutating set based on future results
      }
  }

  // Fix is separate mutable code and keet it small and in separate method
  // see ActorNotLeakingState.scala
}
