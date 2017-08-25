package com.stratio.tikitakka.common.util

import java.net.HttpCookie

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import play.api.libs.json.JsValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait HttpRequestUtils extends LogUtils {

  implicit val system: ActorSystem
  implicit val actorMaterializer: ActorMaterializer

  lazy val httpSystem = Http(system)

  def doRequest[T](uri: String,
                   resource: String,
                   method: HttpMethod = HttpMethods.GET,
                   body: Option[JsValue] = None,
                   cookies: Seq[HttpCookie] = Seq.empty[HttpCookie])(implicit ev: Unmarshaller[ResponseEntity, T]): Future[T] = {
    log.debug(s"Sending HTTP request to [${method.value}] $uri/$resource")
    val request = createRequest(uri, resource, method, body, cookies)
    for {
      response <- httpSystem.singleRequest(request)
      status = {
        val status = response.status.value
        log.debug(s"Status : $status")
        status
      }
      entity <- Unmarshal(response.entity).to[T]
    } yield entity
  }

  private def createRequest(uri: String, resource: String, method: HttpMethod, body: Option[JsValue], cookies: Seq[HttpCookie]): HttpRequest =
    HttpRequest(uri = s"$uri/$resource", method = method, entity = createRequestEntityJson(body), headers = createHeaders(cookies))

  def createRequestEntityJson(body: Option[JsValue]): RequestEntity = body match {
    case Some(jsBody) =>
      log.debug(s"body: ${jsBody.toString()}")
      HttpEntity(MediaTypes.`application/json`, jsBody.toString)
    case _ => HttpEntity.Empty
  }

  def createHeaders(cookies: Seq[HttpCookie]): List[HttpHeader] =
    cookies.map(c => headers.Cookie(c.getName, c.getValue)).toList

}
