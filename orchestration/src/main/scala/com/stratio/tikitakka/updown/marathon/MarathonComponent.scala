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

package com.stratio.tikitakka.updown.marathon

import akka.http.scaladsl.model.HttpMethods._
import com.stratio.tikitakka.common.exceptions._
import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.common.model.CreateApp
import com.stratio.tikitakka.common.model.marathon.MarathonDeleteInfo
import com.stratio.tikitakka.common.model.marathon.MarathonApplication
import com.stratio.tikitakka.common.util.PlayJsonSupport._
import com.stratio.tikitakka.common.util.{ConfigComponent, HttpRequestUtils, LogUtils}
import com.stratio.tikitakka.updown.UpAndDownComponent
import com.stratio.tikitakka.updown.marathon.MarathonComponent._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait MarathonComponent extends UpAndDownComponent with HttpRequestUtils with LogUtils {

  lazy val uri = ConfigComponent.getString(uriField).getOrElse {
    throw ConfigurationException("The marathon uri has not been set")
  }
  lazy val apiVersion = ConfigComponent.getString(versionField, defaultApiVersion)

  val upPath = s"$apiVersion/apps"
  def downPath(appId: String): String = s"$apiVersion/apps/$appId"

  def upApplication(application: CreateApp): Future[ContainerInfo] = {
    val marathonApp = MarathonApplication(application)
    doRequest[MarathonApplication](uri, upPath, upComponentMethod, Option(Json.toJson(marathonApp)))
      .recover { case e: Exception =>
        throw ResponseException("Error when up an application", e)
      }
      .map { case marathonAppResponse =>
        ContainerInfo(marathonAppResponse.id)
      }
  }

  override def downApplication(application: ContainerInfo): Future[ContainerInfo] =
    doRequest[MarathonDeleteInfo](uri, downPath(application.id), downComponentMethod)
      .recover {
        case e: Exception => throw ResponseException("Error when down an application", e)
      }
      .map { case marathonAppResponse =>
        ContainerInfo(marathonAppResponse.deploymentId)
      }
}

object MarathonComponent {

  // Property field constants
  val uriField = "marathon.uri"
  val versionField = "marathon.api.version"

  // Default property constants
  val defaultApiVersion = "v2"

  val upComponentMethod = POST
  val downComponentMethod = DELETE
}
