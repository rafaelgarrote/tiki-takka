package com.stratio.tikitakka.updown.marathon

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.stratio.tikitakka.common.exceptions.ResponseException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp
import com.stratio.tikitakka.common.model.ContainerInfo
import com.stratio.tikitakka.common.model.DockerContainerInfo
import com.stratio.tikitakka.common.model.marathon.MarathonApplication

@RunWith(classOf[JUnitRunner])
class MarathonOrchestratorIT extends WordSpec with ShouldMatchers {

  trait ActorTestSystem {
    implicit val system = ActorSystem("Actor-Test-System")
    implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    implicit val timeout = 10 seconds

  }

  "MarathonOrchestrator" should {
    "Up a application if it is correctly defined" in new MarathonComponent with ActorTestSystem with
      MarathonTestsUtils {
      val application =
        CreateApp(
          id = "app1",
          cpus = 0.2,
          mem = 100,
          instances = Option(1),
          user = None,
          args = None,
          env = None,
          container = ContainerInfo(DockerContainerInfo("centos:7")),
          cmd = Option("tail -f /var/log/yum.log"),
          portDefinitions = None,
          requirePorts = None,
          healthChecks = None,
          labels = Map.empty[String, String]
        )
      val testResult = Try{
        val result: Future[ContainerId] = upApplication(application, None)
        Await.result(result, timeout) shouldBe an[ContainerId]
        Await.result(result, timeout).id shouldBe application.id
      }
      Await.result(destroyApplication(application.id), timeout)
      testResult.get
    }
  }
  "thrown an exception when the endpoint is not correctly defined" in new MarathonComponent with ActorTestSystem {
    override lazy val uri = "http://ocalhost:8080"
    val application =
      CreateApp(
        id = "app1",
        cpus = 0.2,
        mem = 100,
        instances = Option(1),
        user = None,
        args = None,
        env = None,
        container = ContainerInfo(DockerContainerInfo("centos:7")),
        cmd = None,
        portDefinitions = None,
        requirePorts = None,
        healthChecks = None,
        labels = Map.empty[String, String]
      )
    an[ResponseException] should be thrownBy Await.result(upApplication(application, None), timeout)
  }

  "Down a application if it the app is defined" in new MarathonComponent with ActorTestSystem with MarathonTestsUtils {
    val application =
      MarathonApplication(CreateApp(
        id = "app2",
        cpus = 0.2,
        mem = 100,
        instances = Option(1),
        user = None,
        args = None,
        env = None,
        container = ContainerInfo(DockerContainerInfo("centos:7")),
        cmd = Some("tail -f /var/log/yum.log"),
        portDefinitions = None,
        requirePorts = None,
        healthChecks = None,
        labels = Map.empty[String, String]
      ))

    val result: Future[ContainerId] =
      for {
        _ <- createApplication(application)
        result <- downApplication(ContainerId(application.id))
      } yield result

    Await.result(result, timeout) shouldBe an[ContainerId]
  }

  "thrown an exception when the application doesn't exist" in new MarathonComponent with ActorTestSystem {
    override lazy val uri = "http://localhost:8080"

    val application = ContainerId(id = "this-id-is-a-fake")

    an[ResponseException] should be thrownBy Await.result(downApplication(application), timeout)
  }

}
