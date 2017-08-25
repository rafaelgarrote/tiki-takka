package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class CatalogService(
                            Datacenter: String,
                            ID: Option[String],
                            Node: String,
                            Address: String,
                            TaggedAddress: Option[Map[String, String]],
                            NodeMeta: Option[Map[String, String]],
                            Service: AgentService,
                            Check: CheckInfo
                            )


object CatalogService {

  implicit val writer: Writes[CatalogService] = Json.writes[CatalogService]
  implicit val reads: Reads[CatalogService] = Json.reads[CatalogService]
}