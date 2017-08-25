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
