package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class UnregisterService(
                              Datacenter: String,
                              Node: String,
                              ServiceID: String
                              )

object UnregisterService {

  implicit val writer: Writes[UnregisterService] = Json.writes[UnregisterService]
  implicit val reads: Reads[UnregisterService] = Json.reads[UnregisterService]
}
