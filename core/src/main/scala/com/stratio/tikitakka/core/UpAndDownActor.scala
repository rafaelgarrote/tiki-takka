/*
 * Copyright (C) 2017 Stratio (http://stratio.com)
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

import scalaz.Reader
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.pattern.pipe
import com.stratio.tikitakka.common.message._
import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.updown.UpAndDownComponent

class UpAndDownActor(service: UpAndDownComponent) extends Actor with ActorLogging {

  def receive = {
    case UpServiceRequest(buildApp, ssoToken) =>
      service.upApplication(buildApp, ssoToken)
        .map(UpServiceResponse)
        .recover { case (ex: Throwable) =>
          UpServiceFails(ContainerId(buildApp.id), ex.getMessage)
        }
        .pipeTo(sender)

    case DownServiceRequest(appInfo) =>
      service.downApplication(appInfo)
        .map(DownServiceResponse)
        .recover { case (ex: Throwable) =>
          DownServiceFails(appInfo, ex.getMessage)
        }
        .pipeTo(sender)
  }
}

object UpAndDownActor {

  def props = Reader {
    (service: UpAndDownComponent) => Props(classOf[UpAndDownActor], service)
  }

}
