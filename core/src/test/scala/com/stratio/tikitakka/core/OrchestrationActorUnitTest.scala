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

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.stratio.tikitakka.columbus.DiscoveryComponent
import com.stratio.tikitakka.common.message._
import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo
import com.stratio.tikitakka.common.model.{Container, ContainerInfo, CreateApp, PortMapping}
import com.stratio.tikitakka.common.state.ApplicationState
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpecLike}
import com.stratio.tikitakka.common.test.utils.generators.DiscoveryGeneratorUtils._

@RunWith(classOf[JUnitRunner])
class OrchestrationActorUnitTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike
  with ShouldMatchers {

  "OrchestratorActor" should {
    val upAndDownActorDummy = TestProbe()
    val actorRef: TestActorRef[OrchestratorActor] = TestActorRef[OrchestratorActor](Props(classOf[OrchestratorActor], upAndDownActorDummy.ref))
    val actor: OrchestratorActor = actorRef.underlyingActor

    "should obtain an application correctly using messages" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val application = ApplicationState(applicationInfo)
      actor.applications = Map(application.appInfo.id -> application)
      actorRef ! GetApplicationInfo(application.appInfo.id)
      expectMsg(ResponseApplicationState(Option(application)))
    }
    "should return None if the application is not" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val application = ApplicationState(applicationInfo)
      actor.applications = Map.empty[String, ApplicationState]

      actorRef ! GetApplicationInfo(application.appInfo)
      expectMsg(ResponseApplicationState(None))
    }

    "register an application when it is new and send a message with it" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val application = ApplicationState(applicationInfo)
      actor.applications = Map.empty[String, ApplicationState]

      actorRef ! RegisterApplication(applicationInfo)

      actor.applications should contain(applicationInfo.id -> application)
    }

    "update a registered application that exists when Register message is sent and send a message with the new state of the application" in {
      val oldApplicationInfo = genDiscoveryAppInfo.sample.get
      val oldApplication = ApplicationState(oldApplicationInfo)
      val newApplicationInfo = genDiscoveryAppInfo.sample.get.copy(id = oldApplicationInfo.id)
      val newApplication = ApplicationState(newApplicationInfo)
      actor.applications = Map(oldApplicationInfo.id -> oldApplication)

      actorRef ! RegisterApplication(newApplicationInfo)

      actor.applications should contain(newApplication.appInfo.id -> newApplication)
      actor.applications should not contain (oldApplication.appInfo.id -> oldApplication)

    }

    "should return the application that is unregistered using messages" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val application = ApplicationState(applicationInfo)
      actor.applications = Map(applicationInfo.id -> application)

      actorRef ! UnregisterApplication(application.appInfo)

      actor.applications should not contain (application.appInfo.id -> application)
    }

    "should return None when try to unregistered an application and it doesn't exist" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val application = ApplicationState(applicationInfo)

      actorRef ! UnregisterApplication(application.appInfo.id)

    }


    "add a new application state (first application)" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val newApplication = ApplicationState(applicationInfo)
      actor.applications = Map.empty[String, ApplicationState]


      actor.addApplicationState(applicationInfo)

      actor.applications.size should be(1)
      actor.applications should contain((newApplication.appInfo.id, newApplication))
    }

    "add a new application state (not first application)" in {
      val oldApplicationInfo = genDiscoveryAppInfo.sample.get
      val oldApplication = ApplicationState(oldApplicationInfo)
      actor.applications = Map(oldApplicationInfo.id -> oldApplication)
      val newApplicationInfo = genDiscoveryAppInfo.sample.get
      val newApplication = ApplicationState(newApplicationInfo)

      actor.addApplicationState(newApplicationInfo)

      actor.applications.size should be(2)
      actor.applications should contain((newApplication.appInfo.id, newApplication))
      actor.applications should contain((oldApplication.appInfo.id, oldApplication))
    }


    "should update the state of application if the id is in the list" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val oldApplication = ApplicationState(applicationInfo)
      actor.applications = Map(applicationInfo.id -> oldApplication)
      val newApplication = ApplicationState(applicationInfo)

      actor.addApplicationState(applicationInfo)

      actor.applications.size should be(1)
      actor.applications should contain((newApplication.appInfo.id, newApplication))
    }

    "should remove the last element and return it when removes" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val applicationToRemove = ApplicationState(applicationInfo)
      actor.applications = Map(applicationInfo.id -> applicationToRemove)

      val result = actor.removeApplicationState(applicationInfo.id)

      result should be(Option(applicationToRemove))
      actor.applications.size should be(0)
    }

    "should remove an element and return it when removes" in {
      val applicationInfo = genDiscoveryAppInfo.sample.get
      val applicationToRemove = ApplicationState(applicationInfo)
      val applicationInfoOther = genDiscoveryAppInfo.sample.get
      val otherApplication = ApplicationState(applicationInfoOther)
      actor.applications = Map(
        applicationInfo.id -> applicationToRemove,
        applicationInfoOther.id -> otherApplication
      )

      val result = actor.removeApplicationState(applicationInfo.id)

      result should be(Option(applicationToRemove))
      actor.applications should contain(otherApplication.appInfo.id, otherApplication)
      actor.applications.size should be(1)
    }
  }
}
