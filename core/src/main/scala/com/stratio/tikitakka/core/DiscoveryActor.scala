package com.stratio.tikitakka.core

import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.Reader
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.pattern.pipe
import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp
import com.stratio.tikitakka.columbus.DiscoveryComponent
import com.stratio.tikitakka.common.message.DiscoverServices

class DiscoveryActor(service: DiscoveryComponent) extends Actor with ActorLogging {

  def receive = {
    case IsDiscoveryServiceUp =>
      log.debug("Message IsDiscoveryServiceUp received")
      service.isUp pipeTo sender

    case DiscoverServices(tags) =>
      log.debug("Message DiscoverServices received")
      service.discover(tags) pipeTo sender
  }
}

object DiscoveryActor {

  def props = Reader {
    (service: DiscoveryComponent) => Props(classOf[DiscoveryActor], service)
  }

}
