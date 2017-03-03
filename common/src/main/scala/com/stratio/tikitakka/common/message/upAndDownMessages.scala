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
