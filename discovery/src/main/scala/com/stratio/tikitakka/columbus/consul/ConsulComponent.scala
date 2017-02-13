package com.stratio.tikitakka.columbus.consul

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.stratio.tikitakka.columbus.DiscoveryComponent
import com.stratio.tikitakka.common.util.ConfigComponent
import com.stratio.tikitakka.common.util.HttpRequestUtils
import com.stratio.tikitakka.common.util.LogUtils
import com.stratio.tikitakka.common.util.PlayJsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ConsulComponent extends DiscoveryComponent with HttpRequestUtils with LogUtils {

  import ConsulComponent._

  // Config parameters

  val uri = ConfigComponent.config.getString(uriField)
  val version = ConfigComponent.getString(versionField, defaultApiVersion)

  // Discovery Service methods

  def isUp = statusRequests map (_ => true) recover { case _ => false}

  def discover(tags: List[String] = List.empty[String]): Future[Map[String, List[String]]] = {
    discoverRequest map ( response =>
      tags match {
        case x :: _ => response.filter(_._2.map(tags.contains).foldLeft(false) (_ || _))
        case _ => response
      }
    )
  }

  // Aux methods

  private def statusRequests: Future[List[String]] =
    logFunction(DEBUG)("Checking Consul status...") {
      Future.sequence(
        statusResources.map { resource =>
          doRequest[String](uri, s"$version/$resource")
        }
      )
    }

  private def discoverRequest: Future[Map[String, List[String]]] =
    logFunction(DEBUG)(s"Requesting all services...") {
      implicit val unMarshall = PlayJsonSupport.playJsonUnmarshaller[Map[String, List[String]]]
      doRequest[Map[String, List[String]]](uri, s"$version/$servicesResource")
    }

}

object ConsulComponent {

  // Property field constants
  val uriField = "consul.uri"
  val versionField = "consul.api.version"

  // Default property constants
  val defaultApiVersion = "v1"

  // Resource constants
  val statusResources = List("status/leader", "status/peers")
  val servicesResource = "catalog/services"

  def apply(implicit _system: ActorSystem, _materializer: ActorMaterializer): ConsulComponent =
    new ConsulComponent {
      implicit val actorMaterializer: ActorMaterializer = _materializer
      implicit val system: ActorSystem = _system
    }

}
