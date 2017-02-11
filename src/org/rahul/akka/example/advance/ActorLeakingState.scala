package org.rahul.akka.example.advance

import akka.actor.Actor

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
  // see org.rahul.akka.example.advance.ActorNotLeakingState.scala
}
