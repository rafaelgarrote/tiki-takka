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
package com.stratio.tikitakka.common.test.utils.generators

import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo
import com.stratio.tikitakka.common.model.discovery.ServiceInfo
import org.scalacheck.Gen

object DiscoveryGeneratorUtils extends GeneratorUtils {

  def genServiceInfo: Gen[ServiceInfo] = for {
    id <- genID
    name <- genName
    address <- genIP
    port <-genPort
    tags <- genTags
  } yield ServiceInfo(id, name, address, port, tags)

  def genDiscoveryAppInfo: Gen[DiscoveryAppInfo] = for {
    id <- genID
    name <- genName
    size <- Gen.choose(1, 4)
    services <- Gen.listOfN(size, genServiceInfo)
    tags = services.flatMap(_.tags)
  } yield DiscoveryAppInfo(id, name, services, tags)

}
