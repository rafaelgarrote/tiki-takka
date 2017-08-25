package com.stratio.tikitakka.columbus.consul.model

import com.stratio.tikitakka.common.model.discovery.ServiceInfo
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class Service(
                    ID: Option[String], //Future versions will have ID when it happens remove Option
                    Node: String,
                    Address: String,
                    CreateIndex: Int,
                    ModifyIndex: Int,
                    ServiceAddress: String,
                    ServiceEnableTagOverride: Boolean,
                    ServiceID: String,
                    ServiceName: String,
                    ServicePort: Int,
                    ServiceTags: List[String]
                  ) {

  def toDiscoveryServiceInfo: ServiceInfo =
    ServiceInfo(ServiceID, ServiceName, ServiceAddress, ServicePort, ServiceTags)
}

object Service {

  implicit val reads: Reads[Service] = Json.reads[Service]
  implicit val writes: Writes[Service] = Json.writes[Service]
}