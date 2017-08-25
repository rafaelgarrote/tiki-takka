package com.stratio.tikitakka.updown.marathon

import java.net.HttpCookie

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import akka.stream.ActorMaterializer
import com.stratio.tikitakka.common.exceptions._
import com.stratio.tikitakka.common.model.marathon.{MarathonApplication, MarathonDeleteInfo}
import com.stratio.tikitakka.common.model.{ContainerId, CreateApp}
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

  def upApplication(application: CreateApp, ssoToken: Option[HttpCookie]): Future[ContainerId] = {
    val marathonApp = MarathonApplication(application)
    doRequest[MarathonApplication](uri, upPath, upComponentMethod, Option(Json.toJson(marathonApp)), ssoToken.map(List(_)).getOrElse(Seq.empty))
      .recover { case e: Exception =>
        e.printStackTrace()
        throw ResponseException("Error when up an application", e)
      }
      .map { case marathonAppResponse =>
        ContainerId(marathonAppResponse.id)
      }
  }

  override def downApplication(application: ContainerId): Future[ContainerId] =
    doRequest[MarathonDeleteInfo](uri, downPath(application.id), downComponentMethod)
      .recover {
        case e: Exception => throw ResponseException("Error when down an application", e)
      }
      .map { case marathonAppResponse =>
        ContainerId(marathonAppResponse.deploymentId)
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

  def apply(implicit _system: ActorSystem, _materializer: ActorMaterializer): MarathonComponent =
    new MarathonComponent {
      implicit val actorMaterializer: ActorMaterializer = _materializer
      implicit val system: ActorSystem = _system
    }
}
