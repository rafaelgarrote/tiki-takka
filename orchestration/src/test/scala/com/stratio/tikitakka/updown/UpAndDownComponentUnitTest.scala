package com.stratio.tikitakka.updown

import com.stratio.tikitakka.common.exceptions.ResponseException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

import com.stratio.tikitakka.common.model.ContainerId

@RunWith(classOf[JUnitRunner])
class UpAndDownComponentUnitTest extends WordSpec with ShouldMatchers {

  trait ImplicitsValues {
    implicit val timeout = 5 seconds
  }

  "A Orchestration component" should {
    "can up a component" in new DummyUpAndDownComponent with ImplicitsValues {
      Await.result(upApplication(validBuild, None), timeout) shouldBe (right = ContainerId(validBuild.id))
    }

    "cannot up a component if this is not correct" in new DummyUpAndDownComponent with ImplicitsValues {
      an[ResponseException] should be thrownBy Await.result(upApplication(invalidBuild, None), timeout)

    }
    "can down a component" in new DummyUpAndDownComponent with ImplicitsValues {
      Await.result(downApplication(ContainerId(validBuild.id)), timeout) shouldBe (right = ContainerId(validBuild.id))
    }

    "cannot down a component if this is not correct" in new DummyUpAndDownComponent with ImplicitsValues {
      val component = "invalidBuild"
      an[ResponseException] should be thrownBy Await.result(downApplication(ContainerId(invalidBuild.id)), timeout)
    }

  }


}
