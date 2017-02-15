package com.stratio.tikitakka.core

import scala.language.postfixOps
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestProbe
import akka.testkit.{ImplicitSender, TestKit}
import com.stratio.tikitakka.columbus.test.utils.DummyDiscoveryComponent
import com.stratio.tikitakka.common.message.AppDiscovered
import com.stratio.tikitakka.common.message.AppsDiscovered
import com.stratio.tikitakka.common.message.DiscoverService
import com.stratio.tikitakka.common.message.DiscoverServices
import org.junit.runner.RunWith
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers
import com.stratio.tikitakka.common.message.IsDiscoveryServiceUp
import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

@RunWith(classOf[JUnitRunner])
class DiscoveryActorUnitTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "Discovery Actor" should {

    "know if the discovery service is up" in {

      val probe = TestProbe()
      val service = new DummyDiscoveryComponent {
        val uri = upHost
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! IsDiscoveryServiceUp
      expectMsg(true)

    }

    "know if the discovery service is down" in {

      val probe = TestProbe()
      val service = new DummyDiscoveryComponent {
        val uri = "fakeHost"
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! IsDiscoveryServiceUp
      expectMsg(false)

    }

    "return a map with all services discovered with its tags" in {

      val probe = TestProbe()
      val services = Map[String, List[String]]("service" -> List[String]("datasources"))

      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
        override val servicesDiscovered = services
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! DiscoverServices()
      expectMsg(AppsDiscovered(service.servicesDiscovered))
      probe.expectMsg(AppsDiscovered(service.servicesDiscovered))

    }

    "return an empty map if no services discovered" in {

      val probe = TestProbe()
      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! DiscoverServices()
      expectMsg(AppsDiscovered(service.servicesDiscovered))
      probe.expectMsg(AppsDiscovered(service.servicesDiscovered))
    }

    "get a None if the service to discover is not found" in {

      val probe = TestProbe()
      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! DiscoverService("serviceName")
      expectMsg(AppDiscovered(service.serviceDiscovered))

    }

    "get an Option with the service discovered info" in {

      val probe = TestProbe()
      val service = new DummyDiscoveryComponent {
        override val uri: String = upHost
        override val serviceDiscovered = Some(DiscoveryAppInfo("id", "name", List.empty, List[String]("test")))
      }

      val discoveryActor = system.actorOf(Props(classOf[DiscoveryActor],service, probe.ref))

      discoveryActor ! DiscoverService("serviceName")
      expectMsg(AppDiscovered(service.serviceDiscovered))

    }

  }

}