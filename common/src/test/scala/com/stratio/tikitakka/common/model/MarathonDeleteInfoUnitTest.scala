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
import play.api.libs.json.{JsObject, JsString, Json}

import com.stratio.tikitakka.common.model.marathon.MarathonDeleteInfo

@RunWith(classOf[JUnitRunner])
class MarathonDeleteInfoUnitTest extends WordSpec with ShouldMatchers {
  "A marathon response" should {
    "be serialized correctly" in {
      val response = MarathonDeleteInfo("theVersion1", "1223123-123-12-31-23-12")

      val result = Json.toJson(response).asInstanceOf[JsObject]
      result.fields should contain("version", JsString(response.version))
      result.fields should contain("deploymentId", JsString(response.deploymentId))
    }
    "be deserialized correctly" in {
      val expected = MarathonDeleteInfo("theVersion1", "1223123-123-12-31-23-12")
      val json = s"""{"version":"${expected.version}", "deploymentId":"${expected.deploymentId}"}"""

      val result = Json.parse(json).as[MarathonDeleteInfo]

      result should be(expected)
    }
  }
}
