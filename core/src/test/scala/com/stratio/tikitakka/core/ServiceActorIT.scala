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

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.stratio.tikitakka.common.message.{AppDiscovered, DiscoverService, ManageApp, RegisterApplication}
import com.stratio.tikitakka.common.state.ApplicationState
import com.stratio.tikitakka.common.test.utils.generators.DiscoveryGeneratorUtils
import org.junit.runner.RunWith
import org.scalatest.{ShouldMatchers, WordSpecLike}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ServiceActorIT extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "OrchestratorActor" when {
    val discoveryActor = TestProbe()
    val upAndDownActorDummy = TestProbe()
    val appName = "appName"
    "receives messages (RegisterApplication) from ServiceActor" should {
      "register the application if it isn't" in {
        val appInfo = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get
        val orchestratorActorRef = TestActorRef[OrchestratorActor](Props(classOf[OrchestratorActor], upAndDownActorDummy.ref))
        val orchestratorActor = orchestratorActorRef.underlyingActor
        val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActorRef))

        serviceActor ! ManageApp(appName)
        serviceActor ! AppDiscovered(Some(appInfo))

        orchestratorActor.applications.size should be(1)
        orchestratorActor.applications should contain (appInfo.id -> ApplicationState(appInfo))
      }
      "update the application information if it is" in {
        val appInfoOld = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get
        val appInfoNew = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get.copy(id=appInfoOld.id)
        val orchestratorActorRef = TestActorRef[OrchestratorActor](Props(classOf[OrchestratorActor], upAndDownActorDummy.ref))
        val orchestratorActor = orchestratorActorRef.underlyingActor
        val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActorRef))

        orchestratorActorRef ! RegisterApplication(appInfoOld)
        serviceActor ! ManageApp(appName)
        serviceActor ! AppDiscovered(Some(appInfoNew))

        orchestratorActor.applications should contain (appInfoNew.id -> ApplicationState(appInfoNew))
      }
    }
    "receives messages (UnregisterApplication) from ServiceActor" should {
      "unregister the application if it is" in {
        val appInfo = DiscoveryGeneratorUtils.genDiscoveryAppInfo.sample.get
        val orchestratorActorRef = TestActorRef[OrchestratorActor](Props(classOf[OrchestratorActor], upAndDownActorDummy.ref))
        val orchestratorActor = orchestratorActorRef.underlyingActor
        val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActorRef))

        serviceActor ! ManageApp(appName)
        serviceActor ! AppDiscovered(Some(appInfo))
        serviceActor ! AppDiscovered(None)

        orchestratorActor.applications should not contain (appInfo.id -> ApplicationState(appInfo))

      }
    }

    "do nothing if the app is not registered yet" in {
      val orchestratorActorRef = TestActorRef[OrchestratorActor](Props(classOf[OrchestratorActor], upAndDownActorDummy.ref))
      val orchestratorActor = orchestratorActorRef.underlyingActor
      val serviceActor = TestActorRef[ServiceActor](new ServiceActor(discoveryActor.ref, orchestratorActorRef))

      serviceActor ! ManageApp(appName)
      serviceActor ! AppDiscovered(None)

      orchestratorActor.applications.size should be (0)
    }
  }
}
