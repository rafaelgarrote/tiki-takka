package com.stratio.tikitakka.common.message

import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.common.model.CreateApp

sealed trait UpAndDownMessage

case class UpServiceRequest(buildApp: CreateApp) extends UpAndDownMessage
case class UpServiceResponse(appInfo: ContainerInfo) extends UpAndDownMessage
case class UpServiceFails(appInfo: ContainerInfo, msg: String) extends UpAndDownMessage

case class DownServiceRequest(appInfo: ContainerInfo) extends UpAndDownMessage
case class DownServiceResponse(appInfo: ContainerInfo) extends UpAndDownMessage
case class DownServiceFails(appInfo: ContainerInfo, msg: String) extends UpAndDownMessage
