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

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethods, ResponseEntity}
import akka.stream.ActorMaterializer
import com.stratio.tikitakka.common.model.marathon.MarathonApplication
import com.stratio.tikitakka.common.util.HttpRequestUtils
import play.api.libs.json.Json

import scala.concurrent.Future

trait MarathonTestsUtils extends HttpRequestUtils {
  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer
  implicit val uri: String


  def createApplication(application: MarathonApplication): Future[ResponseEntity] = {
    val resource = s"v2/apps"
    doRequest(uri, resource, HttpMethods.POST, Some(Json.toJson(application)))
  }

  def destroyApplication(applicationId: String): Future[ResponseEntity] = {
    val resource = s"v2/apps/$applicationId"
    doRequest(uri, resource, HttpMethods.DELETE)
  }
}
