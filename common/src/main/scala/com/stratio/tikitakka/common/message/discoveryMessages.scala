package com.stratio.tikitakka.common.message

sealed trait DiscoveryMessage

case object IsDiscoveryServiceUp extends DiscoveryMessage

case class DiscoverServices(tags: List[String] = List.empty[String])
case object DiscoverServices extends DiscoveryMessage
