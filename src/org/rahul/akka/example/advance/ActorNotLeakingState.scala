package org.rahul.akka.example.advance

import akka.actor.Actor
import akka.pattern._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Fix is separate mutable code and keet it small and in separate method
// For bad good, see ActorLeakingState.scala

class ActorNotLeakingState extends Actor {

  val isIntSet = mutable.Set.empty[String] // see org.rahul.akka.example.advance.ActorNotLeakingState1 to remove mutable

  def validate(key: String) : Future[Boolean] = Future {
    if(key.contains(" ")) false else true // dummy implementation
  }

  override def receive: Receive = {
    case Add(key) =>
      val list = for (shouldAdd <- validate(key)) yield Validated(key, shouldAdd)
      list.pipeTo(self)

    case Validated(key, isValid) =>
      if(isValid) isIntSet += key
  }
}
