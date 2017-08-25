package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class NodeCatalog(
                        ID: Option[String],
                        Node: String,
                        Address: String,
                        CreateIndex: Int,
                        ModifyIndex: Int
                        )

object NodeCatalog {

  implicit val writer: Writes[NodeCatalog] = Json.writes[NodeCatalog]
  implicit val reads: Reads[NodeCatalog] = Json.reads[NodeCatalog]
}