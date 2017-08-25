package com.stratio.tikitakka.common.model.marathon

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class MarathonDeleteInfo(version: String, deploymentId: String)

object MarathonDeleteInfo {
  implicit val reads:Reads[MarathonDeleteInfo] = Json.reads[MarathonDeleteInfo]
  implicit val writes:Writes[MarathonDeleteInfo] = Json.writes[MarathonDeleteInfo]
}
