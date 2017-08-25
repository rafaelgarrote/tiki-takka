package com.stratio.tikitakka.core

import akka.actor.ActorSystem
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import com.stratio.tikitakka.common.message.AppDiscovered
import com.stratio.tikitakka.common.message.DiscoverService
import com.stratio.tikitakka.common.message.ManageApp
import org.junit.runner.RunWith
import org.scalatest.ShouldMatchers
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner

import com.stratio.tikitakka.common.test.utils.generators.DiscoveryGeneratorUtils

@RunWith(classOf[JUnitRunner])
class ServiceActorTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "Service Actor" should {

    "send a discover service message to discovery actor when a manage app service is recived" in {

      val discoveryActor = TestProbe()
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActor.ref))
      val appName = "appName"

      serviceActor ! ManageApp(appName)
      discoveryActor.expectMsg(DiscoverService(appName))
      serviceActor.underlyingActor.appName shouldBe appName
      serviceActor.underlyingActor.status shouldBe None
    }

    "when a AppDiscovered(Some(DiscoveryAppInfo)) message arrives and the info has changed, the status change" in {

      val appInfo = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get
      val discoveryActor = TestProbe()
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActor.ref))
      val appName = "appName"

      serviceActor ! ManageApp(appName)
      serviceActor ! AppDiscovered(Some(appInfo))
      serviceActor.underlyingActor.appName shouldBe appName
      serviceActor.underlyingActor.status shouldBe Option(appInfo)

    }

    "when a AppDiscovered(Some(DiscoveryAppInfo)) message arrives and the info has NOT changed, the status DO NOT change" in {

      val appInfo = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get
      val discoveryActor = TestProbe()
      val orchestratorActor = TestProbe()
      val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActor.ref))
      val appName = "appName"

      serviceActor ! ManageApp(appName)
      serviceActor.underlyingActor.status = Option(appInfo)
      serviceActor ! AppDiscovered(Some(appInfo))
      serviceActor.underlyingActor.appName shouldBe appName
      serviceActor.underlyingActor.status shouldBe Option(appInfo)

    }

  }

}
