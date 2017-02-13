package com.stratio.tikitakka.columbus.test.utils

import com.stratio.tikitakka.columbus.DiscoveryComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DummyDiscoveryComponent extends DiscoveryComponent {

  val uri: String

  val upHost = "upHost"

  def isUp = if (uri == upHost) Future(true) else Future(false)

}
