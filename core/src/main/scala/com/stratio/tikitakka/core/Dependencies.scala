/*
 * Copyright (C) 2017 Stratio (http://stratio.com)
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
package com.stratio.tikitakka.core

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.stratio.tikitakka.columbus.DiscoveryComponent

trait Dependencies {

  //Actor System
  val actorSystem: ActorSystem
  val materializer: ActorMaterializer

  //Actors
  val discoveryActorRef: ActorRef
  val serviceActorRef: ActorRef
  val servicesActorRef: ActorRef
  val orchestratorActorRef: ActorRef
  val upAndDownActorRef: ActorRef

  val discoveryComponent: DiscoveryComponent

}
