package com.stratio.tikitakka.common.model

import play.api.libs.json.{Json, Reads, Writes}

case class MarathonDeleteInfo(version: String, deploymentId: String)

object MarathonDeleteInfo {
  implicit val reads:Reads[MarathonDeleteInfo] = Json.reads[MarathonDeleteInfo]
  implicit val writes:Writes[MarathonDeleteInfo] = Json.writes[MarathonDeleteInfo]
}
