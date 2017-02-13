package com.stratio.tikitakka.columbus

import scala.concurrent.Future

trait DiscoveryComponent {

  val uri: String

  def isUp: Future[Boolean]

  def discover(tags: List[String] = List.empty[String]): Future[Map[String, List[String]]]

}
