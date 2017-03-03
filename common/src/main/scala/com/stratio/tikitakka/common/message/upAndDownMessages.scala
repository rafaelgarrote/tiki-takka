package com.stratio.tikitakka.common.message

import java.net.HttpCookie

import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp

sealed trait UpAndDownMessage

case class UpServiceRequest(buildApp: CreateApp, ssoToken: Option[HttpCookie]) extends UpAndDownMessage
case class UpServiceResponse(appInfo: ContainerId) extends UpAndDownMessage
case class UpServiceFails(appInfo: ContainerId, msg: String) extends UpAndDownMessage

case class DownServiceRequest(appInfo: ContainerId) extends UpAndDownMessage
case class DownServiceResponse(appInfo: ContainerId) extends UpAndDownMessage
case class DownServiceFails(appInfo: ContainerId, msg: String) extends UpAndDownMessage
