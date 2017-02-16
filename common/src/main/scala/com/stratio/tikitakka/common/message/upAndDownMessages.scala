package com.stratio.tikitakka.common.message

import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp

sealed trait UpAndDownMessage

case class UpServiceRequest(buildApp: CreateApp) extends UpAndDownMessage
case class UpServiceResponse(appInfo: ContainerId) extends UpAndDownMessage
case class UpServiceFails(appInfo: ContainerId, msg: String) extends UpAndDownMessage

case class DownServiceRequest(appInfo: ContainerId) extends UpAndDownMessage
case class DownServiceResponse(appInfo: ContainerId) extends UpAndDownMessage
case class DownServiceFails(appInfo: ContainerId, msg: String) extends UpAndDownMessage
