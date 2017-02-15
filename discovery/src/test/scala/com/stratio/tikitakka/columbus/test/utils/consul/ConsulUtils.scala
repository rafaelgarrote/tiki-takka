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
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import NodeCatalog._

trait ConsulUtils extends LazyLogging {

  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer
  implicit val uri: String

  lazy val httpSystem = Http(system)

  def registerServices(services: List[CatalogService]): Future[List[HttpResponse]] =
    Future.sequence(services.map(registerService))

  def registerService(service: CatalogService): Future[HttpResponse] = {
    val resource = "v1/catalog/register"
    val body = Json.stringify(Json.toJson(service))
    doRequest(uri, resource, HttpMethods.PUT, Some(body))
  }

  def unregisterServices(services: List[UnregisterService]): Future[List[HttpResponse]] =
    Future.sequence(services.map(unregisterService))

  def unregisterService(service: UnregisterService): Future[HttpResponse] = {
    val resource = s"v1/catalog/deregister"
    val body = Json.stringify(Json.toJson(service))
    doRequest(uri, resource, HttpMethods.PUT, Some(body))
  }

  def getDatacenter: Future[String] =
    for {
      response <- doRequest(uri, "v1/catalog/datacenters", HttpMethods.GET)
      result <- Unmarshal(response.entity).to[String]
    } yield Json.parse(result).as[List[String]].head

  def getNode: Future[NodeCatalog] = {
    for {
      response <- doRequest(uri, "v1/catalog/nodes", HttpMethods.GET)
      result <- Unmarshal(response.entity).to[String]
    } yield Json.parse(result).as[List[NodeCatalog]].head
  }

  private def doRequest(uri: String,
                resource: String,
                method: HttpMethod = HttpMethods.GET,
                body: Option[String] = None): Future[HttpResponse] = {
    val request = createRequest(uri, resource, method, body)
    httpSystem.singleRequest(request)
  }

  private def createRequest(uri: String, resource: String, method: HttpMethod, body: Option[String]): HttpRequest = {
    HttpRequest(uri = s"$uri/$resource", method = method,
      entity = body.map(body => HttpEntity(MediaTypes.`application/json`, body)).getOrElse(""))
  }
}
