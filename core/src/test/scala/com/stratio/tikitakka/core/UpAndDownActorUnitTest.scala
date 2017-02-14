package com.stratio.tikitakka.core

import scala.language.postfixOps
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.junit.runner.RunWith
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers
import com.stratio.tikitakka.common.message._
import com.stratio.tikitakka.common.model.AppInfo
import com.stratio.tikitakka.updown.DummyUpAndDownComponent

@RunWith(classOf[JUnitRunner])
class UpAndDownActorUnitTest extends TestKit(ActorSystem("MySpec"))
with ImplicitSender
with WordSpecLike
with ShouldMatchers {

  "Up and Down Actor" should {

    val service = new DummyUpAndDownComponent {}

    "create a new service" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! UpServiceRequest(service.validBuild)
      expectMsg(UpServiceResponse(AppInfo(service.validBuild.id)))
    }

    "receive an error when create a new service fails" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! UpServiceRequest(service.invalidBuild)
      expectMsg(UpServiceFails(AppInfo(service.invalidBuild.id), "Error when up"))
    }

    "kill a service" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! DownServiceRequest(AppInfo(service.validBuild.id))
      expectMsg(DownServiceResponse(AppInfo(service.validBuild.id)))
    }

    "receive an error when kill a service fails" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! DownServiceRequest(AppInfo(service.invalidBuild.id))
      expectMsg(DownServiceFails(AppInfo(service.invalidBuild.id), "Error when down"))
    }
  }
}