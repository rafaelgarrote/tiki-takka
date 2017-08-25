package com.stratio.tikitakka.common.message

import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

sealed trait DiscoveryMessage

case object IsDiscoveryServiceUp extends DiscoveryMessage

case class DiscoverServices(tags: List[String] = List.empty[String])
case object DiscoverServices extends DiscoveryMessage

case class DiscoverService(serviceName: String)
case object DiscoverService extends DiscoveryMessage

case class AppDiscovered(appInfo: Option[DiscoveryAppInfo])
case object AppDiscovered  extends DiscoveryMessage

case class AppsDiscovered(appsDiscovered: Map[String, List[String]])
case object AppsDiscovered  extends DiscoveryMessage

case class ManageApp(name: String)
case object ManageApp extends DiscoveryMessage

case class AppUnDiscovered(name: String)
case object AppUnDiscovered extends DiscoveryMessage
