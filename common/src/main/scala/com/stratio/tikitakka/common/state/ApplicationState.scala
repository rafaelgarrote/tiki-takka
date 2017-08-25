package com.stratio.tikitakka.common.state

import com.stratio.tikitakka.common.model.Container
import com.stratio.tikitakka.common.model.discovery.DiscoveryAppInfo

case class ApplicationState(appInfo: DiscoveryAppInfo, containers: Seq[Container] = Seq.empty[Container])
