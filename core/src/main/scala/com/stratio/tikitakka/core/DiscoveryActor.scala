package com.stratio.tikitakka.core

import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.Reader

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.pattern.pipe

import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp
import com.stratio.tikitakka.columbus.DiscoveryComponent

class DiscoveryActor(service: DiscoveryComponent) extends Actor with ActorLogging {

  def receive = {
    case IsDiscoveryServiceUp =>
      log.debug("Message IsDiscoveryServiceUp received")
      service.isUp pipeTo sender
  }
}

object DiscoveryActor {

  def props = Reader {
    (service: DiscoveryComponent) => Props(classOf[DiscoveryActor], service)
  }

}
