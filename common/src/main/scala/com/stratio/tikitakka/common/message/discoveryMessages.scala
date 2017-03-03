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
package com.stratio.tikitakka.common.message

import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

sealed trait DiscoveryMessage

case object IsDiscoveryServiceUp extends DiscoveryMessage

case class DiscoverServices(tags: List[String] = List.empty[String])
case object DiscoverServices extends DiscoveryMessage

case class DiscoverService(serviceName: String)
case object DiscoverService extends DiscoveryMessage

case class AppDiscovered(appInfo: Option[DiscoveryAppInfo])
case object AppDiscovered  extends DiscoveryMessage

case class AppsDiscovered(appsDiscovered: Map[String, List[String]])
case object AppsDiscovered  extends DiscoveryMessage

case class ManageApp(name: String)
case object ManageApp extends DiscoveryMessage

case class AppUnDiscovered(name: String)
case object AppUnDiscovered extends DiscoveryMessage
