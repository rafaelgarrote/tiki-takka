package com.stratio.tikitakka.common.model

trait Container

case class CreateApp(id: String,
                     cpus: Double,
                     mem: Int,
                     instances: Int,
                     container: ContainerInfo,
                     cmd: Option[String] = None,
                     healthChecks: Seq[HealthCheck] = Seq.empty[HealthCheck],
                     labels: Map[String, String] = Map.empty[String, String]) extends Container

case class ContainerId(id: String)

case class ContainerInfo(image: String, portMappings: Seq[PortMapping])

case class PortMapping(hostPort: Int, containerPort: Int)

case class HealthCheck(id: String)

