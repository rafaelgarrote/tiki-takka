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
                               instances: Int,
                               container: MarathonContainer,
                               cmd: Option[String],
                               healthChecks: Seq[MarathonHealthCheck],
                               labels: Map[String, String]) extends ApplicationModels {
}

object MarathonApplication {

  def apply(buildApp: BuildApp): MarathonApplication =
    MarathonApplication(
      id = buildApp.id,
      cpus = buildApp.cpus,
      mem = buildApp.mem,
      instances = buildApp.instances,
      containerId = buildApp.container.image,
      cmd = buildApp.cmd,
      labels = buildApp.labels
    )

  def apply(id: String, cpus: Double, mem: Int, instances: Int, containerId: String, cmd: Option[String] = None,
            labels: Map[String, String] = Map.empty[String, String]): MarathonApplication =
    new MarathonApplication(
      id,
      cpus,
      mem,
      instances,
      MarathonContainer(Docker(containerId, Seq.empty[DockerPortMapping])),
      cmd,
      Seq.empty[MarathonHealthCheck],
      labels)

  def fromJson(id: String,
               cpus: Double,
               mem: Int,
               instances: Int,
               container: MarathonContainer,
               cmd: Option[String],
               healthChecks: Seq[MarathonHealthCheck],
               labels: Map[String, String]) =
    MarathonApplication(id.replaceFirst("^/", ""), cpus, mem, instances, container, cmd, healthChecks, labels)

  // Literals
  val idLiteral = "id"
  val cpusLiteral = "cpus"
  val memLiteral = "mem"
  val instancesLiteral = "instances"
  val containerLiteral = "container"
  val cmdLiteral = "cmd"
  val healthChecksLiteral = "healthChecks"
  val labelsLiteral = "labels"

  implicit val writes: Writes[MarathonApplication] = Json.writes[MarathonApplication]
  implicit val reads: Reads[MarathonApplication] = (
    (__ \ idLiteral).read[String] and
      (__ \ cpusLiteral).read[Double] and
      (__ \ memLiteral).read[Int] and
      (__ \ instancesLiteral).read[Int] and
      (__ \ containerLiteral).read[MarathonContainer] and
      (__ \ cmdLiteral).readNullable[String] and
      (__ \ healthChecksLiteral).read[Seq[MarathonHealthCheck]] and
      (__ \ labelsLiteral).read[Map[String, String]]
    )(MarathonApplication.fromJson _)
}

case class MarathonContainer(docker: Docker, `type`: String = "DOCKER")

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

  implicit val dockerWrites: Writes[Docker] = Json.writes[Docker]
  implicit val reads: Reads[Docker] = Json.reads[Docker]
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
