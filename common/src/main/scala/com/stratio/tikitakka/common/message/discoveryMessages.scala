package com.stratio.tikitakka.common.message

sealed trait DiscoveryMessage

case object IsDiscoveryServiceUp extends DiscoveryMessage

