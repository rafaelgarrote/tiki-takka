package com.stratio.tikitakka.core

import scala.language.postfixOps
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.junit.runner.RunWith
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers
import com.stratio.tikitakka.common.message._
import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.updown.DummyUpAndDownComponent

@RunWith(classOf[JUnitRunner])
class UpAndDownActorTest extends TestKit(ActorSystem("MySpec"))
with ImplicitSender
with WordSpecLike
with ShouldMatchers {

  "Up and Down Actor" should {

    val service = new DummyUpAndDownComponent {}

    "create a new service" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! UpServiceRequest(service.validBuild)
      expectMsg(UpServiceResponse(ContainerId(service.validBuild.id)))
    }

    "receive an error when create a new service fails" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! UpServiceRequest(service.invalidBuild)
      expectMsg(UpServiceFails(ContainerId(service.invalidBuild.id), "Error when up"))
    }

    "kill a service" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! DownServiceRequest(ContainerId(service.validBuild.id))
      expectMsg(DownServiceResponse(ContainerId(service.validBuild.id)))
    }

    "receive an error when kill a service fails" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! DownServiceRequest(ContainerId(service.invalidBuild.id))
      expectMsg(DownServiceFails(ContainerId(service.invalidBuild.id), "Error when down"))
    }
  }
}