package com.stratio.tikitakka.columbus

import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

import scala.concurrent.Future

trait DiscoveryComponent {

  val uri: String

  def isUp: Future[Boolean]

  def discover(tags: List[String] = List.empty[String]): Future[Map[String, List[String]]]

  def discover(serviceName: String): Future[Option[DiscoveryAppInfo]]
}
