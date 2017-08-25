package com.stratio.tikitakka.updown.marathon

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ShouldMatchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class MarathonComponentUnitTest extends WordSpec with ShouldMatchers {

  trait ActorTestSystem {
    implicit val system = ActorSystem("Actor-Test-System")
    implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
  }

  "Marathon Orchestrator" should {
    "recover the uri correctly" in new MarathonComponent with ActorTestSystem {
      uri shouldBe "http://localhost:8080"
    }
  }
}
