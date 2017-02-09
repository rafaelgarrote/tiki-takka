package com.stratio.tikitakka.columbus.consul

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import com.stratio.tikitakka.common.util.ConfigComponent
import com.stratio.tikitakka.common.util.HttpRequestUtils
import com.stratio.tikitakka.common.util.LogUtils
import com.stratio.tikitakka.columbus.DiscoveryComponent

trait ConsulComponent extends DiscoveryComponent with HttpRequestUtils with LogUtils {

  import ConsulComponent._

  // Config parameters

  val uri = ConfigComponent.config.getString(uriField)
  val version = ConfigComponent.getString(versionField, defaultApiVersion)

  // Discovery Service methods

  def isUp = statusRequests map (_ => true) recover { case _ => false}

  // Aux methods

  private def statusRequests: Future[List[String]] =
    logFunction(DEBUG)("Checking Consul status...") {
      Future.sequence(
        statusResources.map { resource =>
          doRequest[String](uri, s"$version/$resource")
        }
      )
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

  def apply(implicit _system: ActorSystem, _materializer: ActorMaterializer): ConsulComponent =
    new ConsulComponent {
      implicit val actorMaterializer: ActorMaterializer = _materializer
      implicit val system: ActorSystem = _system
    }

}
