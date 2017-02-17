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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp
import com.stratio.tikitakka.common.model.ContainerInfo

trait DummyUpAndDownComponent extends UpAndDownComponent {

  val validBuild =
    CreateApp("validBuild", 0.2, 256, Option(2), None, None, None, container = ContainerInfo("containerId", Seq(),
      None), None, None, None, Map.empty[String, String])

  val invalidBuild =
    CreateApp("invalidBuild", 0.2, 256, Option(2), None, None, None,
      container = ContainerInfo("containerId", Seq(), None), None, None, None, Map.empty[String, String])

  def upApplication(application: CreateApp): Future[ContainerId] = Future {
    if (application == validBuild) ContainerId(validBuild.id)
    else throw ResponseException("Error when up", new Exception)
  }

  def downApplication(application: ContainerId): Future[ContainerId] = Future {
    if (application.id == validBuild.id) ContainerId(validBuild.id)
    else throw ResponseException("Error when down", new Exception)
  }
}
