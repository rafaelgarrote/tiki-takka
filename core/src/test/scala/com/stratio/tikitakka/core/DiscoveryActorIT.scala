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
package com.stratio.tikitakka.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import com.stratio.tikitakka.columbus.consul.ConsulComponent
import com.stratio.tikitakka.columbus.test.utils.consul.AgentService
import com.stratio.tikitakka.columbus.test.utils.consul.ConsulUtils
import com.stratio.tikitakka.common.message.AppsDiscovered
import com.stratio.tikitakka.common.message.DiscoverServices
import com.stratio.tikitakka.common.util.ConfigComponent
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.ShouldMatchers
import org.scalatest.WordSpecLike
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DiscoveryActorIT extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with ShouldMatchers
  with BeforeAndAfterAll
  with ConsulUtils{

  implicit val testSystem = ActorSystem("test")
  implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
  implicit val uri = ConfigComponent.config.getString(ConsulComponent.uriField)

  val services = (0 to 4).map(_ => AgentService.randomObject)
  val servicesMap = services.map(s => s.Name -> s.Tags) ++ Map[String, List[String]]("consul" -> List.empty[String])

  override def beforeAll(): Unit = {
    registerServices(services.toList)
  }

  "Discovery Actor" should {

    "Discover new services" in {

      val orchestratorActor = TestProbe()
      val service = ConsulComponent(system, actorMaterializer)
      val serviceActor = TestActorRef[ServicesActor](new ServicesActor(orchestratorActor.ref))
      val discoveryActor = TestActorRef[DiscoveryActor](new DiscoveryActor(service, serviceActor))

      discoveryActor ! DiscoverServices(List.empty[String])
      expectMsg(AppsDiscovered(servicesMap.toMap))
    }
  }

  override def afterAll(): Unit = {
    unregisterServices(services.toList)
    Thread.sleep(10000)
  }
}
