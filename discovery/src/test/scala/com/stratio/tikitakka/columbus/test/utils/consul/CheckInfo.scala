package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class CheckInfo(
                      Node: String,
                      CheckID: String,
                      Name: String,
                      Notes: String,
                      Status: String,
                      ServiceID: String
                      )

object CheckInfo {

  implicit val writer: Writes[CheckInfo] = Json.writes[CheckInfo]
  implicit val reads: Reads[CheckInfo] = Json.reads[CheckInfo]

}
