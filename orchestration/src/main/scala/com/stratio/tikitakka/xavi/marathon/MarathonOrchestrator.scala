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
package com.stratio.tikitakka.xavi.marathon

import akka.http.scaladsl.model.HttpMethods._
import com.stratio.tikitakka.common.exceptions._
import com.stratio.tikitakka.common.model.{MarathonApplication, MarathonDeleteInfo}
import com.stratio.tikitakka.common.util.PlayJsonSupport._
import com.stratio.tikitakka.common.util.{ConfigComponent, HttpRequestUtils, LogUtils}
import com.stratio.tikitakka.xavi.Orchestrator
import com.stratio.tikitakka.xavi.marathon.MarathonOrchestrator._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait MarathonOrchestrator extends Orchestrator[MarathonApplication] with HttpRequestUtils with LogUtils {

  type UpResponse = MarathonApplication
  type DownResponse = MarathonDeleteInfo


  lazy val uri: String = ConfigComponent.getString(uriField).getOrElse(throw ConfigurationException("The marathon uri has not been set"))

  val apiVersion: String = ConfigComponent.getString(versionField, defaultApiVersion)
  val upPath = s"$apiVersion/apps"

  def downPath(appId: String): String = s"$apiVersion/apps/$appId"

  def upApplication(component: MarathonApplication): Future[UpResponse] = {
    doRequest[UpResponse](uri, upPath, upComponentMethod, Option(Json.toJson(component))).recover {
      case e: Exception => throw ResponseException("Error when up an application", e)
    }
  }

  override def downApplication(component: MarathonApplication): Future[DownResponse] = {
    doRequest[DownResponse](uri, downPath(component.id), downComponentMethod).recover {
      case e: Exception => throw ResponseException("Error when down an application", e)
    }
  }


}

object MarathonOrchestrator {

  // Property field constants
  val uriField = "marathon.uri"
  val versionField = "marathon.api.version"

  // Default property constants
  val defaultApiVersion = "v2"

  val upComponentMethod = POST
  val downComponentMethod = DELETE


}
