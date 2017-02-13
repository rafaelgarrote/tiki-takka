package com.stratio.tikitakka.core

import scala.language.postfixOps
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.stratio.tikitakka.columbus.test.utils.DummyDiscoveryComponent
import com.stratio.tikitakka.common.message.DiscoverServices
import org.junit.runner.RunWith
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers
import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp

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

    "return a map with all services discovered with its tags" in {

      val services = Map[String, List[String]]("service" -> List[String]("datasources"))

      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
        override val servicesDiscovered = services
      }

      val discoveryActor = system.actorOf(DiscoveryActor.props(service))

      discoveryActor ! DiscoverServices()
      expectMsg(service.servicesDiscovered)

    }

    "return an map if no services discovered" in {

      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
      }

      val discoveryActor = system.actorOf(DiscoveryActor.props(service))

      discoveryActor ! DiscoverServices()
      expectMsg(service.servicesDiscovered)

    }

  }

}