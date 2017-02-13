package com.stratio.tikitakka.columbus.test.utils

import com.stratio.tikitakka.columbus.DiscoveryComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DummyDiscoveryComponent extends DiscoveryComponent {

  val uri: String

  val upHost = "upHost"
  val servicesDiscovered = Map.empty[String, List[String]]

  def isUp = if (uri == upHost) Future(true) else Future(false)

  def discover(tags: List[String] = List.empty[String]): Future[Map[String, List[String]]] =
    Future(servicesDiscovered)

}
