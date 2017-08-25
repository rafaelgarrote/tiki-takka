package com.stratio.tikitakka.core

import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import com.stratio.tikitakka.columbus.test.utils.consul.AgentService
import com.stratio.tikitakka.common.message.AppUnDiscovered
import com.stratio.tikitakka.common.message.AppsDiscovered
import org.junit.runner.RunWith
import org.scalatest.ShouldMatchers
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ServicesActorTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "Services Actor" should {

    "create new child actors when a Apps Discovered message arrived and the apps are not managed" in {

      val services = (0 to 4).map(_ => AgentService.randomObject)
      val servicesDiscovered = services.map(s => s.Service -> s.Tags).toMap
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServicesActor](new ServicesActor(orchestratorActor.ref))

      serviceActor ! AppsDiscovered(servicesDiscovered)
      serviceActor.underlyingActor.services.keySet should contain theSameElementsAs servicesDiscovered.keySet
      servicesDiscovered.keySet.foreach(k => serviceActor.underlyingActor.context.child(k) should not be None)

    }

    "when a Apps Discovered message arrive create just the new childs for apps are not already managed" in {

      val services = (0 to 4).map(_ => AgentService.randomObject)
      val servicesDiscovered = services.map(s => s.Service -> s.Tags).toMap
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServicesActor](new ServicesActor(orchestratorActor.ref))
      serviceActor.underlyingActor.services = Map[String, Cancellable](servicesDiscovered.keySet.head -> null)

      serviceActor ! AppsDiscovered(servicesDiscovered)
      serviceActor.underlyingActor.services.keySet should contain theSameElementsAs servicesDiscovered.keySet
      servicesDiscovered.keySet.tail.foreach(k => serviceActor.underlyingActor.context.child(k) should not be None)

    }

    "when a AppUnDiscovered message arrive, cancel child actor and remove it form services map" in {

      val services = (0 to 4).map(_ => AgentService.randomObject)
      val servicesDiscovered = services.map(s => s.Service -> s.Tags).toMap
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServicesActor](new ServicesActor(orchestratorActor.ref))

      serviceActor ! AppsDiscovered(servicesDiscovered)
      serviceActor ! AppUnDiscovered(servicesDiscovered.keySet.head)

      serviceActor.underlyingActor.services.keySet should contain theSameElementsAs servicesDiscovered.keySet.tail

    }
  }

}
