package com.stratio.tikitakka.updown

import java.net.HttpCookie

import scala.concurrent.Future
import com.stratio.tikitakka.common.model.ContainerId
import com.stratio.tikitakka.common.model.CreateApp

trait UpAndDownComponent {

  def upApplication(application: CreateApp, ssoToken: Option[HttpCookie]): Future[ContainerId]

  def downApplication(application: ContainerId): Future[ContainerId]

}
