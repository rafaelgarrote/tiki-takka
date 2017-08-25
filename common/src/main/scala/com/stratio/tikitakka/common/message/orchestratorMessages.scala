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
