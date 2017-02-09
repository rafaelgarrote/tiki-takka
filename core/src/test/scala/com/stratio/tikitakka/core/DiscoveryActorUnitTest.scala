package com.stratio.tikitakka.core

import scala.language.postfixOps

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, ImplicitSender }
import org.junit.runner.RunWith
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers

import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp
import com.stratio.tikitakka.columbus.DummyDiscoveryComponent

@RunWith(classOf[JUnitRunner])
class DiscoveryActorUnitTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "Discovery Actor" should {

    "know if the discovery service is up" in {

      val service = new DummyDiscoveryComponent {
        val uri = upHost
      }

      val discoveryActor = system.actorOf(DiscoveryActor.props(service))

      discoveryActor ! IsDiscoveryServiceUp
      expectMsg(true)

    }

    "know if the discovery service is down" in {

      val service = new DummyDiscoveryComponent {
        val uri = "fakeHost"
      }

      val discoveryActor = system.actorOf(DiscoveryActor.props(service))

      discoveryActor ! IsDiscoveryServiceUp
      expectMsg(false)

    }
  }

}