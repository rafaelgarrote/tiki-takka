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
package com.stratio.tikitakka.columbus

import com.stratio.tikitakka.columbus.test.utils.DummyDiscoveryComponent

import scala.concurrent.Await
import scala.concurrent.duration._
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class DiscoveryComponentUnitTest extends WordSpec with ShouldMatchers {

  val timeout = 3 seconds

  "DiscoveryComponent" should {

    "know if the discovery service is up" in new DummyDiscoveryComponent {

      val uri = upHost

      Await.result(isUp, timeout) should be(true)

    }

    "know if the discovery service is down" in new DummyDiscoveryComponent {

      val uri = "fakeHost"

      Await.result(isUp, timeout) should be(false)

    }
  }

}
