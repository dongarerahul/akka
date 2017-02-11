import java.util.logging.Logger

import akka.actor.Actor

//no mutable state outside actor context
class MyGoodActor extends Actor {

  val logger = Logger.getLogger("MyGoodActor")

  override def receive = active(Set.empty)

  def active(isInSet: Set[String]) : Receive = {
    case Add(key)       => logger.info("MyGoodctor -> Add Message Received !"); context become active(isInSet + key)
    case Contains(key)  => logger.info("MyGoodActor -> Contains Message Received !"); sender() ! isInSet(key)
  }
}
