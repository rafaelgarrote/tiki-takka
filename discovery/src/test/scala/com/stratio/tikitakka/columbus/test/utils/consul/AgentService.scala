package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class AgentService(
                        ID: String,
                        Service: String,
                        Tags: List[String],
                        Address: String,
                        Port: Int
                      ) {


  def toCatalogService(datacenter: String, nodeCatalog: NodeCatalog): CatalogService =
    CatalogService(
      datacenter,
      nodeCatalog.ID,
      nodeCatalog.Node,
      nodeCatalog.Address,
      None,
      None,
      this,
      CheckInfo(nodeCatalog.Node, s"service:$ID", "", "", "passing", ID)
    )
}

object AgentService {

  implicit val writer: Writes[AgentService] = Json.writes[AgentService]
  implicit val reads: Reads[AgentService] = Json.reads[AgentService]

  def randomObject = ConsulGeneratorUtils.genAgentService.sample.get
}
