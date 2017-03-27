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
package com.stratio.tikitakka.updown

import java.net.HttpCookie

import com.stratio.tikitakka.common.exceptions.ResponseException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp
import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.common.model.DockerContainerInfo

trait DummyUpAndDownComponent extends UpAndDownComponent {

  val validBuild =
    CreateApp(
      id = "validBuild",
      cpus = 0.2,
      mem = 256,
      instances = Option(2),
      container = ContainerInfo(DockerContainerInfo("containerId")),
      labels = Map.empty[String, String])

  val invalidBuild =
    CreateApp(
      id = "invalidBuild",
      cpus = 0.2,
      mem = 256,
      instances = Option(2),
      container = ContainerInfo(DockerContainerInfo("containerId")),
      labels = Map.empty[String, String])

  def upApplication(application: CreateApp,  ssoToken: Option[HttpCookie]): Future[ContainerId] = Future {
    if (application == validBuild) ContainerId(validBuild.id)
    else throw ResponseException("Error when up", new Exception)
  }

  def downApplication(application: ContainerId): Future[ContainerId] = Future {
    if (application.id == validBuild.id) ContainerId(validBuild.id)
    else throw ResponseException("Error when down", new Exception)
  }
}
