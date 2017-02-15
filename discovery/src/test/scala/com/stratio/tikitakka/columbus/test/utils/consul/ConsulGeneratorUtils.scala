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

import com.stratio.tikitakka.common.test.utils.generators.GeneratorUtils
import org.scalacheck.Gen

object ConsulGeneratorUtils extends GeneratorUtils {

  def genAgentService: Gen[AgentService] = for {
    id <- genID
    service = id
    tags <- genTags
    address <- genIP
    port <- Gen.choose(80, 50000)
  } yield AgentService(id, service, tags, address, port)
}
