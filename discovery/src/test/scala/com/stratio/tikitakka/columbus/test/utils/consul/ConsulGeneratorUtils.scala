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
