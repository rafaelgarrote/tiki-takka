package com.stratio.tikitakka.common.model.discovery

case class DiscoveryAppInfo(id: String, name: String, services: List[ServiceInfo], tags: List[String])
