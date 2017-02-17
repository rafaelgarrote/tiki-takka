package com.stratio.tikitakka.common.model

import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Container

case class CreateApp(id: String,
                     cpus: Double,
                     mem: Int,
                     instances: Option[Int],
                     user: Option[String],
                     args: Option[String] = None,
                     env: Option[Map[String, String]] = None,
                     container: ContainerInfo,
                     cmd: Option[String] = None,
                     portDefinitions: Option[Seq[PortDefinition]],
                     healthChecks: Option[Seq[HealthCheck]] = None,
                     labels: Map[String, String] = Map.empty[String, String]) extends Container

case class ContainerId(id: String)

case class ContainerInfo(image: String, portMappings: Seq[PortMapping], volumes: Option[Seq[Volume]])

object ContainerInfo {

  implicit val writes: Writes[ContainerInfo] = Json.writes[ContainerInfo]
  implicit val reads: Reads[ContainerInfo] = Json.reads[ContainerInfo]

}

object CreateApp {

  implicit val writes: Writes[CreateApp] = Json.writes[CreateApp]
  implicit val reads: Reads[CreateApp] = Json.reads[CreateApp]

}

case class Volume(containerPath: String, hostPath: String, mode: String)

object Volume {

  implicit val writes: Writes[Volume] = Json.writes[Volume]
  implicit val reads: Reads[Volume] = Json.reads[Volume]

}

case class PortMapping(hostPort: Int, containerPort: Int)

object PortMapping {

  implicit val writes: Writes[PortMapping] = Json.writes[PortMapping]
  implicit val reads: Reads[PortMapping] = Json.reads[PortMapping]

}

case class PortDefinition(name: Option[String], port: Int, protocol: String, labels: Map[String, String])

object PortDefinition {

  implicit val writes: Writes[PortDefinition] = Json.writes[PortDefinition]
  implicit val reads: Reads[PortDefinition] = Json.reads[PortDefinition]
}

case class HealthCheck(protocol: String, command: HealthCheckCommand, gracePeriodSeconds: Int, intervalSeconds: Int,
                       timeoutSeconds: Int, maxConsecutiveFailures: Int, ignoreHttp1xx: Boolean)

object HealthCheck {

  implicit val writes: Writes[HealthCheck] = Json.writes[HealthCheck]
  implicit val reads: Reads[HealthCheck] = Json.reads[HealthCheck]
}

case class HealthCheckCommand(value: String)

object HealthCheckCommand {

  implicit val writes: Writes[HealthCheckCommand] = Json.writes[HealthCheckCommand]
  implicit val reads: Reads[HealthCheckCommand] = Json.reads[HealthCheckCommand]
}

