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
                               healthChecks: Seq[MarathonHealthCheck],
                               labels: Map[String, String]) extends Container {
}

object MarathonApplication {

  def apply(buildApp: CreateApp): MarathonApplication =
    MarathonApplication(
      id = buildApp.id,
      cpus = buildApp.cpus,
      mem = buildApp.mem,
      instances = buildApp.instances,
      user = buildApp.user,
      args = buildApp.args,
      env = buildApp.env,
      container = buildApp.container,
      cmd = buildApp.cmd,
      labels = buildApp.labels
    )

  def apply(
             id: String,
             cpus: Double,
             mem: Int,
             instances: Option[Int],
             user: Option[String],
             args: Option[String],
             env: Option[Map[String, String]],
             container: ContainerInfo,
             cmd: Option[String] = None,
             labels: Map[String,String] = Map.empty[String, String]): MarathonApplication =
    new MarathonApplication(
      id,
      cpus,
      mem,
      instances,
      user,
      args,
      env,
      MarathonContainer(
        Docker(
          container.image,
          container.portMappings.map { case PortMapping(p1, p2) =>
            DockerPortMapping(p1, p2)
          }
        ),
        "DOCKER",
        container.volumes.map { volumes =>
          volumes.map { case Volume(containerPath, hostPath, mode) =>
            MarathonVolume(containerPath, hostPath, mode)
          }
        }
      ),
      cmd,
      Seq.empty[MarathonHealthCheck],
      labels)

  def fromJson(id: String,
               cpus: Double,
               mem: Int,
               instances: Option[Int],
               user: Option[String],
               args: Option[String],
               env: Option[Map[String, String]],
               container: MarathonContainer,
               cmd: Option[String],
               healthChecks: Seq[MarathonHealthCheck],
               labels: Map[String, String]) =
    MarathonApplication(
      id.replaceFirst("^/", ""), cpus, mem, instances, user, args, env, container, cmd, healthChecks, labels
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
      (__ \ healthChecksLiteral).read[Seq[MarathonHealthCheck]] and
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

case class MarathonHealthCheck(id: String)

object MarathonHealthCheck {

  implicit val writes: Writes[MarathonHealthCheck] = Json.writes[MarathonHealthCheck]
  implicit val reads: Reads[MarathonHealthCheck] = Json.reads[MarathonHealthCheck]
}
