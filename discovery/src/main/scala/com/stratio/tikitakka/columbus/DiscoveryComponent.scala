package com.stratio.tikitakka.columbus

import scala.concurrent.Future

trait DiscoveryComponent {

  val uri: String

  def isUp: Future[Boolean]

}
