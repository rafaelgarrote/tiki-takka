package com.stratio.tikitakka.common.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.JsValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait HttpRequestUtils extends LazyLogging {

  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer

  lazy val httpSystem = Http(system)

  def doRequest[T](uri: String,
                   resource: String,
                   method: HttpMethod = HttpMethods.GET,
                   body: Option[JsValue] = None)(implicit ev: Unmarshaller[ResponseEntity, T]): Future[T] = {
    logger.debug(s"Sending HTTP request to $uri")
    val request = createRequest(uri, resource, method, body)
    for {
      response <- httpSystem.singleRequest(request)
      entity <- Unmarshal(response.entity).to[T]
    } yield entity
  }

  private def createRequest(uri: String, resource: String, method: HttpMethod, body: Option[JsValue]): HttpRequest =
    HttpRequest(uri = s"$uri/$resource", method = method, entity = createRequestEntityJson(body))

  def createRequestEntityJson(body: Option[JsValue]): RequestEntity = body match {
    case Some(jsBody) => HttpEntity(MediaTypes.`application/json`, jsBody.toString)
    case _ => HttpEntity.Empty
  }


}
