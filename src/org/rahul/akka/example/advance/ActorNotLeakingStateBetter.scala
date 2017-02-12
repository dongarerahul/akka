package org.rahul.akka.example.advance

import java.util.logging.Logger

import akka.actor.{Actor, ActorRef}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Mutating Context here is nothing but context update to new function
// For bad good, see ActorLeakingState.scala
class ActorNotLeakingStateBetter extends Actor {

  val logger = Logger.getLogger("ActorNotLeakingState1")

  def validate(key: String) : Future[Boolean] = Future {
    if(key.contains(" ")) false else true // dummy implementation
  }

  override def receive: Receive = idle(Set.empty)

  def idle(isIntSet: Set[String]) : Receive = {
    case Add(key) =>
      logger.info(s"*************** Received Idle :: org.rahul.akka.example.simple.Add($key) with param :: $isIntSet")
      //validate(key) map(Validated(key, _)) pipeTo(self)
      context.become(waitForValidation(isIntSet, sender()))
      logger.info(s"*************** Ending Idle :: org.rahul.akka.example.simple.Add($key) with param :: $isIntSet")

    case IsPresent(key) =>
      logger.info(s"*************** Received Idle :: IsPresent($key)")
      val isPresent = if(isIntSet.contains(key)) true else false
      logger.info(s"*************** Idle :: $key is $isPresent")
      sender() ! isPresent
      logger.info(s"*************** Ending Idle :: IsPresent($key)")
  }

  def waitForValidation(set: Set[String], source: ActorRef): Receive = {
    case Validated(key, isValid) =>
      logger.info(s"*************** Received waitForValidation :: Validated($key) with param :: $set")
      val newSet = if(isValid) set + key else set
      source ! Continue
      context.become(idle(newSet))
      logger.info(s"*************** Ending waitForValidation :: Validated($key) with param :: $set")

    case Add(input) =>
      logger.info(s"**************** waitForValidation->org.rahul.akka.example.simple.Add($input)")
      source ! Reject
      logger.info(s"**************** Context is still waitForValidation. Hence Rejected -> org.rahul.akka.example.simple.Add($input)")

    case IsPresent(key) =>
      logger.info(s"*************** Received waitForValidation :: IsPresent($key)")
      val isPresent = if(set.contains(key)) true else false
      logger.info(s"*************** Idle :: $key is $isPresent")
      sender() ! isPresent
      logger.info(s"*************** Ending waitForValidation :: IsPresent($key)")
  }
}
