package com.stratio.tikitakka.common.model.discovery

case class ServiceInfo(id: String, name: String, address: String, port: Int, tags: List[String])
