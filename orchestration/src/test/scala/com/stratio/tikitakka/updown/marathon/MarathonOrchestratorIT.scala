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
package com.stratio.tikitakka.updown.marathon

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.stratio.tikitakka.common.exceptions.ResponseException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.common.model.CreateApp
import com.stratio.tikitakka.common.model.Container
import com.stratio.tikitakka.common.model.marathon.MarathonApplication

@RunWith(classOf[JUnitRunner])
class MarathonOrchestratorIT extends WordSpec with ShouldMatchers {

  trait ActorTestSystem {
    implicit val system = ActorSystem("Actor-Test-System")
    implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    implicit val timeout = 10 seconds

  }

  "MarathonOrchestrator" should {
    "Up a application if it is correctly defined" in new MarathonComponent with ActorTestSystem with
      MarathonTestsUtils {
      val application =
        CreateApp(
          id = "app1",
          cpus = 0.2,
          mem = 100,
          instances = 1,
          container = Container("centos:7", Seq()),
          cmd = Option("tail -f /var/log/yum.log")
        )
      val testResult = Try{
        val result: Future[ContainerInfo] = upApplication(application)
        Await.result(result, timeout) shouldBe an[ContainerInfo]
        Await.result(result, timeout).id shouldBe application.id
      }
      Await.result(destroyApplication(application.id), timeout)
      testResult.get
    }
  }
  "thrown an exception when the endpoint is not correctly defined" in new MarathonComponent with ActorTestSystem {
    override lazy val uri = "http://ocalhost:8080"
    val application = CreateApp("app1", 0.2, 100, 1, Container("centos:7", Seq()))
    an[ResponseException] should be thrownBy Await.result(upApplication(application), timeout)
  }

  "Down a application if it the app is defined" in new MarathonComponent with ActorTestSystem with MarathonTestsUtils {
    val application = MarathonApplication("app2", 0.2, 100, 1, "centos:7", Some("tail -f /var/log/yum.log"))

    val result: Future[ContainerInfo] =
      for {
        _ <- createApplication(application)
        result <- downApplication(ContainerInfo(application.id))
      } yield result

    Await.result(result, timeout) shouldBe an[ContainerInfo]
  }

  "thrown an exception when the application doesn't exist" in new MarathonComponent with ActorTestSystem {
    override lazy val uri = "http://localhost:8080"

    val application = ContainerInfo(id = "this-id-is-a-fake")

    an[ResponseException] should be thrownBy Await.result(downApplication(application), timeout)
  }

}
