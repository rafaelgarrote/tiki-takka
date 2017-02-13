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
package com.stratio.tikitakka.columbus.test.utils.consul

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.stream.ActorMaterializer
import play.api.libs.json.Json

import scala.concurrent.Future

trait ConsulUtils {

  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer
  implicit val uri: String

  lazy val httpSystem = Http(system)

  def registerServices(services: List[AgentService]): Unit = services.foreach(registerService)

  def registerService(service: AgentService): Unit = {
    val resource = "v1/agent/service/register"
    val body = Json.stringify(Json.toJson(service))
    doRequest(uri, resource, HttpMethods.PUT, Some(body))
  }

  def unregisterServices(services: List[AgentService]) = services.foreach(unregisterService)

  def unregisterService(service: AgentService): Unit = {
    val resource = s"v1/agent/service/deregister/${service.ID}"
    doRequest(uri, resource, HttpMethods.GET)
  }

  private def doRequest(uri: String,
                resource: String,
                method: HttpMethod = HttpMethods.GET,
                body: Option[String] = None): Future[HttpResponse] = {
    val request = createRequest(uri, resource, method, body)
    httpSystem.singleRequest(request)
  }

  private def createRequest(uri: String, resource: String, method: HttpMethod, body: Option[String]): HttpRequest = {
    HttpRequest(uri = s"$uri/$resource", method = method, entity = body.map(body => HttpEntity(MediaTypes
      .`application/json`, body)).getOrElse(""))
  }
}
