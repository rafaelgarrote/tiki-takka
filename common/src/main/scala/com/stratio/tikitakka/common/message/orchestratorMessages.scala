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
import com.stratio.tikitakka.common.state.ApplicationState

sealed trait OrchestratorMessages

case class RegisterApplication(appId: DiscoveryAppInfo) extends OrchestratorMessages

case class GetApplicationInfo(appId: String) extends OrchestratorMessages

object GetApplicationInfo {
  def apply(app: DiscoveryAppInfo) = new GetApplicationInfo(app.id)
}

case class UnregisterApplication(appId: String) extends OrchestratorMessages

object UnregisterApplication {
  def apply(app: DiscoveryAppInfo) = new UnregisterApplication(app.id)
}

case class ResponseApplicationState(appId: Option[ApplicationState]) extends OrchestratorMessages
