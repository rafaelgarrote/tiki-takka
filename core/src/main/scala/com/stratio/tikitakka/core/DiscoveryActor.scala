package com.stratio.tikitakka.core

import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.Reader
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.pipe
import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp
import com.stratio.tikitakka.columbus.DiscoveryComponent
import com.stratio.tikitakka.common.message.AppDiscovered
import com.stratio.tikitakka.common.message.AppsDiscovered
import com.stratio.tikitakka.common.message.DiscoverService
import com.stratio.tikitakka.common.message.DiscoverServices

class DiscoveryActor(service: DiscoveryComponent, servicesActor: ActorRef) extends Actor with ActorLogging {

  def receive = {
    case IsDiscoveryServiceUp =>
      log.debug("Message IsDiscoveryServiceUp received")
      service.isUp pipeTo sender

    case DiscoverServices(tags) =>
      log.debug("Message DiscoverServices received")
      val result = service.discover(tags) map (AppsDiscovered(_))
      result pipeTo sender
      result pipeTo servicesActor

    case DiscoverService(serviceName) =>
      log.debug(s"Message DiscoverService - $serviceName received")
      service.discover(serviceName) map (AppDiscovered(_)) pipeTo sender

    case _ => log.debug(s"Unknown message")
  }
}

object DiscoveryActor {

  def props = Reader {
    (dependencies: Dependencies) =>
      Props(classOf[DiscoveryActor], dependencies.discoveryComponent, dependencies.servicesActorRef)
  }

}
