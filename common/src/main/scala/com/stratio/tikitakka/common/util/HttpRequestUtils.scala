package com.stratio.tikitakka.common.util

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging

trait HttpRequestUtils extends LazyLogging {

  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer

  lazy val httpSystem = Http(system)

  def doRequest[T](uri: String,
                   resource: String,
                   method: HttpMethod = HttpMethods.GET,
                   body: Option[String] = None)(implicit ev: Unmarshaller[ResponseEntity,T]): Future[T] = {
    logger.debug(s"Sending HTTP request to $uri")
    val request = createRequest(uri, resource, method, body)
    for {
      response <- httpSystem.singleRequest(request)
      entity <- Unmarshal(response.entity).to[T]
    } yield entity
  }

  private def createRequest(uri: String, resource: String, method: HttpMethod, body: Option[String]): HttpRequest =
    HttpRequest(uri = s"$uri/$resource", method = method, entity = ByteString(body.getOrElse("")))
}
