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
package com.stratio.tikitakka.common.model

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}
import play.api.libs.json._

import com.stratio.tikitakka.common.model.marathon._

@RunWith(classOf[JUnitRunner])
class MarathonApplicationUnitTest extends WordSpec with ShouldMatchers {

  "MarathonComponent" should {
    "be written as json correctly" in {
      val labelsMap = Map("thisIsAKey" -> "thisIsAValue", "ThisIsAnotherKey" -> "ThisIsAnotherValue")
      val component =
        marathon.MarathonApplication(
          id = "andId",
          cpus = 0.2,
          mem = 256,
          instances = Option(2),
          user = None,
          args = None,
          env = Some(Map()),
          container = MarathonContainer(Docker("containerId", Seq()), "DOCKER", None),
          cmd = Some("bash -x ls"),
          None,
          None,
          labels = labelsMap
        )

      val result: JsObject = Json.toJson(component).asInstanceOf[JsObject]

      result.fields should contain("id", JsString(component.id))
      result.fields should contain("cpus", JsNumber(component.cpus))
      result.fields should contain("instances", JsNumber(component.instances.get))
      result.fields should contain("mem", JsNumber(component.mem))
      val labels = result.value("labels").as[JsObject]
      labels.value.mapValues(_.asInstanceOf[JsString].value) should equal(labelsMap)
      val jsDocker = result.value("container").asInstanceOf[JsObject].value("docker").asInstanceOf[JsObject]
      jsDocker.fields should contain("image", JsString(component.container.docker.image))
      jsDocker.fields should contain("network", JsString("BRIDGE"))
    }

    "be read fron json correctly" in {
      val expectedComponent =
        marathon.MarathonApplication(
          "app1", 0.2, 100, Option(1), None, None, Some(Map()),
          MarathonContainer(Docker("centos:7", Seq(DockerPortMapping(12, 21))), "DOCKER", None),
          None, Option(Seq(MarathonPortDefinition(None, 0, "tcp", Map.empty[String, String]))), Some(List()),
          Map("tag" -> "tag1,tag2"))

      val text =
        io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("component-marathon.json")).mkString

      val json = Json.parse(text)
      val jsonComponent = json.asInstanceOf[JsArray].head.get
      val resultComponent = jsonComponent.as[marathon.MarathonApplication]
      resultComponent shouldBe expectedComponent
    }
  }
}
