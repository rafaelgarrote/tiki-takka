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

package com.stratio.tikitakka.common.model.marathon

import play.api.libs.functional.syntax._
import play.api.libs.json._

import com.stratio.tikitakka.common.model._

case class MarathonApplication(id: String,
                               cpus: Double,
                               mem: Int,
                               instances: Option[Int],
                               user: Option[String],
                               args: Option[String],
                               env: Option[Map[String, String]],
                               container: MarathonContainer,
                               cmd: Option[String],
                               portDefinitions: Option[Seq[MarathonPortDefinition]],
                               healthChecks: Option[Seq[MarathonHealthCheck]],
                               labels: Map[String, String]) extends Container {
}

object MarathonApplication {

  def apply(buildApp: CreateApp): MarathonApplication =
    new MarathonApplication(
      buildApp.id,
      buildApp.cpus,
      buildApp.mem,
      buildApp.instances,
      buildApp.user,
      buildApp.args,
      buildApp.env,
      MarathonContainer(
        Docker(
          buildApp.container.image,
          buildApp.container.portMappings.map { case PortMapping(p1, p2) =>
            DockerPortMapping(p1, p2)
          }
        ),
        "DOCKER",
        buildApp.container.volumes.map { volumes =>
          volumes.map { case Volume(containerPath, hostPath, mode) =>
            MarathonVolume(containerPath, hostPath, mode)
          }
        }
      ),
      buildApp.cmd,
      buildApp.portDefinitions.map { portDefinitions =>
        portDefinitions.map { portDefinition =>
          MarathonPortDefinition(
            portDefinition.name,
            portDefinition.port,
            portDefinition.protocol,
            portDefinition.labels
          )
        }
      },
      buildApp.healthChecks.map { healthChecks =>
        healthChecks.map { healthCheck =>
          MarathonHealthCheck(healthCheck.protocol, MarathonHealthCheckCommand(healthCheck.command.value),
            healthCheck.gracePeriodSeconds, healthCheck.intervalSeconds, healthCheck.timeoutSeconds,
            healthCheck.maxConsecutiveFailures, healthCheck.ignoreHttp1xx)
        }
      },
      buildApp.labels
    )

  def fromJson(id: String,
               cpus: Double,
               mem: Int,
               instances: Option[Int],
               user: Option[String],
               args: Option[String],
               env: Option[Map[String, String]],
               container: MarathonContainer,
               cmd: Option[String],
               portDefinitions: Option[Seq[MarathonPortDefinition]],
               healthChecks: Option[Seq[MarathonHealthCheck]],
               labels: Map[String, String]) =
    MarathonApplication(
      id.replaceFirst("^/", ""), cpus, mem, instances, user, args, env, container, cmd, portDefinitions, healthChecks, labels
    )

  // Literals
  val idLiteral = "id"
  val cpusLiteral = "cpus"
  val memLiteral = "mem"
  val instancesLiteral = "instances"
  val userLiteral = "user"
  val argsLiteral = "args"
  val envLiteral = "env"
  val containerLiteral = "container"
  val cmdLiteral = "cmd"
  val portDefinitionsLiteral = "portDefinitions"
  val healthChecksLiteral = "healthChecks"
  val labelsLiteral = "labels"

  implicit val writes: Writes[MarathonApplication] = Json.writes[MarathonApplication]
  implicit val reads: Reads[MarathonApplication] = (
    (__ \ idLiteral).read[String] and
      (__ \ cpusLiteral).read[Double] and
      (__ \ memLiteral).read[Int] and
      (__ \ instancesLiteral).readNullable[Int] and
      (__ \ userLiteral).readNullable[String] and
      (__ \ argsLiteral).readNullable[String] and
      (__ \ envLiteral).readNullable[Map[String, String]] and
      (__ \ containerLiteral).read[MarathonContainer] and
      (__ \ cmdLiteral).readNullable[String] and
      (__ \ portDefinitionsLiteral).readNullable[Seq[MarathonPortDefinition]] and
      (__ \ healthChecksLiteral).readNullable[Seq[MarathonHealthCheck]] and
      (__ \ labelsLiteral).read[Map[String, String]]
    )(MarathonApplication.fromJson _)
}

case class MarathonContainer(docker: Docker, `type`: String = "DOCKER", volume: Option[Seq[MarathonVolume]])

object MarathonContainer {

  val dockerLiteral: String = "docker"
  val typeLiteral: String = "type"

  implicit val writes: Writes[MarathonContainer] = Json.writes[MarathonContainer]
  implicit val reads: Reads[MarathonContainer] = Json.reads[MarathonContainer]
}

case class Docker(image: String, portMappings: Seq[DockerPortMapping], network: String = "BRIDGE")

object Docker {

  val imageLiteral: String = "image"
  val portMappingsLiteral: String = "portMappings"
  val networkLiteral: String = "network"

  implicit val writes: Writes[Docker] = Json.writes[Docker]
  implicit val reads: Reads[Docker] = Json.reads[Docker]
}

case class MarathonVolume(containerPath: String, hostPath: String, mode: String)

object MarathonVolume {

  val containerPathLiteral: String = "containerPath"
  val hostPathLiteral: String = "hostPath"
  val modeLiteral: String = "mode"

  implicit val writes: Writes[MarathonVolume] = Json.writes[MarathonVolume]
  implicit val reads: Reads[MarathonVolume] = Json.reads[MarathonVolume]
}

case class DockerPortMapping(hostPort: Int, containerPort: Int)

object DockerPortMapping {

  val hostPortLiteral: String = "hostPort"
  val containerPortLiteral: String = "containerPort"

  implicit val writes: Writes[DockerPortMapping] = Json.writes[DockerPortMapping]
  implicit val reads: Reads[DockerPortMapping] = Json.reads[DockerPortMapping]
}

case class MarathonPortDefinition(name: Option[String], port: Int, protocol: String, labels: Map[String, String])

object MarathonPortDefinition {

  implicit val writes: Writes[MarathonPortDefinition] = Json.writes[MarathonPortDefinition]
  implicit val reads: Reads[MarathonPortDefinition] = Json.reads[MarathonPortDefinition]
}

case class MarathonHealthCheck(protocol: String, command: MarathonHealthCheckCommand, gracePeriodSeconds: Int,
                               intervalSeconds: Int, timeoutSeconds: Int, maxConsecutiveFailures: Int,
                               ignoreHttp1xx: Boolean)

object MarathonHealthCheck {

  implicit val writes: Writes[MarathonHealthCheck] = Json.writes[MarathonHealthCheck]
  implicit val reads: Reads[MarathonHealthCheck] = Json.reads[MarathonHealthCheck]
}

case class MarathonHealthCheckCommand(value: String)

object MarathonHealthCheckCommand {

  implicit val writes: Writes[MarathonHealthCheckCommand] = Json.writes[MarathonHealthCheckCommand]
  implicit val reads: Reads[MarathonHealthCheckCommand] = Json.reads[MarathonHealthCheckCommand]
}
