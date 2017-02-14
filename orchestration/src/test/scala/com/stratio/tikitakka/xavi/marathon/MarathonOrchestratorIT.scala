/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
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
package com.stratio.tikitakka.xavi.marathon

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ResponseEntity
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.stratio.tikitakka.common.exceptions.ResponseException
import com.stratio.tikitakka.common.model.{MarathonApplication, MarathonDeleteInfo}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class MarathonOrchestratorIT extends WordSpec with ShouldMatchers {

  trait ActorTestSystem {
    implicit val system = ActorSystem("Actor-Test-System")
    implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    implicit val timeout = 10 seconds

  }

  "MarathonOrchestrator" should {
    "Up a component if it is correctly defined" in new MarathonOrchestrator with ActorTestSystem with MarathonTestsUtils {
      val component = MarathonApplication("app1", 0.2, 100, 1, "centos:7", Some("tail -f /var/log/yum.log"))
      val testResult = Try{
        val result: Future[MarathonApplication] = upApplication(component)
        Await.result(result, timeout) shouldBe an[MarathonApplication]
        Await.result(result, timeout).id shouldBe component.id
      }
      Await.result(destroyApplication(component.id), timeout)
      testResult.get
    }
  }
  "thrown an exception when the endpoint is not correctly defined" in new MarathonOrchestrator with ActorTestSystem {
    override lazy val uri = "http://ocalhost:8080"
    val component = MarathonApplication("app1", 0.2, 100, 1, "centos:7")
    an[ResponseException] should be thrownBy Await.result(upApplication(component), timeout)
  }

  "Down a component if it the app is defined" in new MarathonOrchestrator with ActorTestSystem with MarathonTestsUtils {
    val component = MarathonApplication("app2", 0.2, 100, 1, "centos:7", Some("tail -f /var/log/yum.log"))
    val creation: Future[ResponseEntity] = createApplication(component)
    Await.result(creation, timeout)

    val result: Future[MarathonDeleteInfo] = downApplication(component)

    Await.result(result, timeout) shouldBe an[MarathonDeleteInfo]
  }

  "thrown an exception when the application doesn't exist" in new MarathonOrchestrator with ActorTestSystem {
    override lazy val uri = "http://localhost:8080"

    val component = MarathonApplication("this-id-is-a-fake", 0.2, 100, 1, "centos:7")

    an[ResponseException] should be thrownBy Await.result(downApplication(component), timeout)
  }

}
