package com.stratio.tikitakka.columbus

import scala.concurrent.Await
import scala.concurrent.duration._

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class DiscoveryComponentUnitTest extends WordSpec with ShouldMatchers {

  val timeout = 3 seconds

  "DiscoveryComponent" should {

    "know if the discovery service is up" in new DummyDiscoveryComponent {

      val uri = upHost

      Await.result(isUp, timeout) should be(true)

    }

    "know if the discovery service is down" in new DummyDiscoveryComponent {

      val uri = "fakeHost"

      Await.result(isUp, timeout) should be(false)

    }
  }

}
