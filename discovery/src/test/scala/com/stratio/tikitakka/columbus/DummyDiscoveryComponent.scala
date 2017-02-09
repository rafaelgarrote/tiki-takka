package com.stratio.tikitakka.columbus

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DummyDiscoveryComponent extends DiscoveryComponent {

  val uri: String

  val upHost = "upHost"

  def isUp = if (uri == upHost) Future(true) else Future(false)

}
