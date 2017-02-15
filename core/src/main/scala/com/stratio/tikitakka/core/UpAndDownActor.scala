package com.stratio.tikitakka.core

import scalaz.Reader
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.pattern.pipe
import com.stratio.tikitakka.common.message._
import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.updown.UpAndDownComponent

class UpAndDownActor(service: UpAndDownComponent) extends Actor with ActorLogging {

  def receive = {
    case UpServiceRequest(buildApp) =>
      service.upApplication(buildApp)
        .map(UpServiceResponse)
        .recover { case (ex: Throwable) =>
          UpServiceFails(ContainerInfo(buildApp.id), ex.getMessage)
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
