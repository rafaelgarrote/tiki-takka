package com.stratio.tikitakka.columbus.consul

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import com.stratio.tikitakka.columbus.test.utils.consul.AgentService
import com.stratio.tikitakka.columbus.test.utils.consul.ConsulUtils
import com.stratio.tikitakka.common.util.ConfigComponent
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.ShouldMatchers
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class ConsulComponentIT extends WordSpec with ShouldMatchers with BeforeAndAfterAll with ConsulUtils {

  implicit val system = ActorSystem("Actor-Test-System")
  implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
  implicit val uri = ConfigComponent.config.getString(ConsulComponent.uriField)

  val datasourceTags = List[String]("datasource")
  val agentTags = List[String]("dg-agent")
  val allTags = datasourceTags ++ agentTags
  val datasourceServices = (0 to 5).map(_ => AgentService.randomObject.copy(Tags = datasourceTags))
  val agentServices = (0 to 5).map(_ => AgentService.randomObject.copy(Tags = agentTags))
  val services = datasourceServices ++ agentServices
  val datasourceServiceMap = datasourceServices.map(s => s.Name -> s.Tags).toMap
  val agentServiceMap = agentServices.map(s => s.Name -> s.Tags).toMap
  val taggedServicesMap = datasourceServiceMap ++ agentServiceMap
  val servicesMap = taggedServicesMap ++ Map[String, List[String]]("consul" -> List.empty[String])

  trait ActorTestSystem {

    implicit val system: ActorSystem = ActorSystem("Actor-Test-System")
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    val timeout = 3 seconds

  }

  override def beforeAll(): Unit = {
    Try {
      registerServices(services.toList)
    }
  }

  "ConsulComponent" should {

    "know if the discovery service is up" in new ConsulComponent with ActorTestSystem {

      Await.result(isUp, timeout) should be(true)

    }

    "know if the discovery service is down" in new ConsulComponent with ActorTestSystem {

      override val uri = "fakeHost"

      Await.result(isUp, timeout) should be(false)

    }

    "get all services and its info from discovery service" in new ConsulComponent with ActorTestSystem {

      Await.result(discover(), timeout) should equal(servicesMap)

    }

    "get services and its info filtered by its tags from discovery service" in new ConsulComponent
      with ActorTestSystem {

      Await.result(discover(datasourceTags), timeout) should equal(datasourceServiceMap)
      Await.result(discover(agentTags), timeout) should equal(agentServiceMap)
      Await.result(discover(allTags), timeout) should equal(taggedServicesMap)

    }

  }

  override def afterAll(): Unit = {
    unregisterServices(services.toList)
    Thread.sleep(5000)
  }

}
