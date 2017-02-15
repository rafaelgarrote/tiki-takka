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
package com.stratio.tikitakka.columbus.test.utils

import com.stratio.tikitakka.columbus.DiscoveryComponent
import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DummyDiscoveryComponent extends DiscoveryComponent {

  val uri: String

  val upHost = "upHost"
  val servicesDiscovered = Map.empty[String, List[String]]
  val serviceDiscovered: Option[DiscoveryAppInfo] = None

  def isUp = if (uri == upHost) Future(true) else Future(false)

  def discover(tags: List[String] = List.empty[String]): Future[Map[String, List[String]]] =
    Future(servicesDiscovered)

  def discover(serviceName: String): Future[Option[DiscoveryAppInfo]] = Future(serviceDiscovered)

}
