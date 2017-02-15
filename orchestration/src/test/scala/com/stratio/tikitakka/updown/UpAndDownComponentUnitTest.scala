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
package com.stratio.tikitakka.updown

import com.stratio.tikitakka.common.exceptions.ResponseException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

import com.stratio.tikitakka.common.model.ContainerInfo

@RunWith(classOf[JUnitRunner])
class UpAndDownComponentUnitTest extends WordSpec with ShouldMatchers {

  trait ImplicitsValues {
    implicit val timeout = 5 seconds
  }

  "A Orchestration component" should {
    "can up a component" in new DummyUpAndDownComponent with ImplicitsValues {
      Await.result(upApplication(validBuild), timeout) shouldBe (right = ContainerInfo(validBuild.id))
    }

    "cannot up a component if this is not correct" in new DummyUpAndDownComponent with ImplicitsValues {
      an[ResponseException] should be thrownBy Await.result(upApplication(invalidBuild), timeout)

    }
    "can down a component" in new DummyUpAndDownComponent with ImplicitsValues {
      Await.result(downApplication(ContainerInfo(validBuild.id)), timeout) shouldBe (right = ContainerInfo(validBuild.id))
    }

    "cannot down a component if this is not correct" in new DummyUpAndDownComponent with ImplicitsValues {
      val component = "invalidBuild"
      an[ResponseException] should be thrownBy Await.result(downApplication(ContainerInfo(invalidBuild.id)), timeout)
    }

  }


}
