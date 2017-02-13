/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.tikitakka.columbus.test.utils.consul

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class AgentService(
                        ID: String,
                        Name: String,
                        Tags: List[String],
                        Address: String,
                        Port: Int,
                        EnableTagOverride: Boolean
                      )

object AgentService {

  implicit val writer: Writes[AgentService] = Json.writes[AgentService]
  implicit val reads: Reads[AgentService] = Json.reads[AgentService]

  def randomObject = ConsulGeneratorUtils.genAgentService.sample.get

}
