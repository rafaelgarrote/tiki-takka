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

import org.scalacheck.Gen

object ConsulGeneratorUtils {

  val exampleServiceName = List[String]("elastic", "postgres", "cassandra", "dg-agent")

  def genTags: Gen[List[String]] =
    for {
      sz <- Gen.choose(0, 4)
      tags <- Gen.listOfN(sz, Gen.oneOf("datasource", "elasticsearch", "postgresql", "cassandra"))
    }  yield tags.distinct

  def genIP: Gen[String] = for {
    x <- Gen.choose(127,186)
    y <- Gen.choose(0,18)
    z <- Gen.choose(0,18)
  } yield s"$x.0.$y.$z"

  def genID: Gen[String] = for {
    name <- Gen.oneOf[String](exampleServiceName)
    n <- Gen.choose(1, 1000000)
  } yield s"$name-$n"

  def genAgentService: Gen[AgentService] = for {
    id <- genID
    service = id
    tags <- genTags
    address <- genIP
    port <- Gen.choose(80, 50000)
  } yield AgentService(id, service, tags, address, port)
}
