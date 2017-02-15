package com.stratio.tikitakka.common.model

trait ApplicationModels

case class CreateApp(id: String,
                     cpus: Double,
                     mem: Int,
                     instances: Int,
                     container: Container,
                     cmd: Option[String] = None,
                     healthChecks: Seq[HealthCheck] = Seq.empty[HealthCheck],
                     labels: Map[String, String] = Map.empty[String, String]) extends ApplicationModels

case class ContainerInfo(id: String)

case class Container(image: String, portMappings: Seq[PortMapping])

case class PortMapping(hostPort: Int, containerPort: Int)

case class HealthCheck(id: String)

