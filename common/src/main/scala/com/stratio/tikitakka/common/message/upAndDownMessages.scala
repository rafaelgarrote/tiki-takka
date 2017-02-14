package com.stratio.tikitakka.common.message

import com.stratio.tikitakka.common.model.AppInfo
import com.stratio.tikitakka.common.model.BuildApp

sealed trait UpAndDownMessage

case class UpServiceRequest(buildApp: BuildApp) extends UpAndDownMessage
case class UpServiceResponse(appInfo: AppInfo) extends UpAndDownMessage
case class UpServiceFails(appInfo: AppInfo, msg: String) extends UpAndDownMessage

case class DownServiceRequest(appInfo: AppInfo) extends UpAndDownMessage
case class DownServiceResponse(appInfo: AppInfo) extends UpAndDownMessage
case class DownServiceFails(appInfo: AppInfo, msg: String) extends UpAndDownMessage
