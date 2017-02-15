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

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Cancellable
import akka.actor.Kill
import akka.actor.Props
import com.stratio.tikitakka.common.message.AppUnDiscovered
import com.stratio.tikitakka.common.message.AppsDiscovered
import com.stratio.tikitakka.common.message.ManageApp
import com.stratio.tikitakka.common.util.ConfigComponent

import scalaz.Reader
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ServicesActor(orchestrator: ActorRef) extends Actor with ActorLogging {

  var services = Map[String, Cancellable]()
  lazy val discoverInitialDelayInSeconds = ConfigComponent.getInt("discover.initial.delay.in.seconds", 5) second
  lazy val discoverIntervalInSeconds = ConfigComponent.getInt("discover.interval.in.seconds", 5) second

  override def receive: Receive = {

    case AppsDiscovered(appsDiscovered) => appsDiscovered.foreach{ app =>
      services.get(app._1) match {
        case Some(_) => log.debug(s"[${app._1}] already managed")
        case None =>
          val actorRef = context.actorOf(Props(classOf[ServiceActor], sender, orchestrator), app._1)
          val cancellable = context.system.scheduler.schedule(
            discoverInitialDelayInSeconds,
            discoverIntervalInSeconds,
            actorRef,
            ManageApp(app._1)
          )
          services = services + (app._1 -> cancellable)
      }
    }

    case AppUnDiscovered(appName) =>
      services.get(appName).map(_.cancel()).foreach(status => if(status) services = services - appName)
      context.child(appName).foreach(_ ! Kill)

    case _ => log.debug(s"Unknown message")
  }
}

object ServicesActor {

  def props = Reader {
    (dependencies: Dependencies) => Props(classOf[ServicesActor], dependencies.orchestratorActorRef)
  }
}
