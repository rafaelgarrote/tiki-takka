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

      upAndDownActor ! UpServiceRequest(service.validBuild, None)
      expectMsg(UpServiceResponse(ContainerId(service.validBuild.id)))
    }

    "receive an error when create a new service fails" in {

      val upAndDownActor = system.actorOf(UpAndDownActor.props(service))

      upAndDownActor ! UpServiceRequest(service.invalidBuild, None)
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