/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
