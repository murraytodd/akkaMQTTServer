package com.murrraywilliams.mqtt

import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.{Future, Await}
import com.murraywilliams.mqtt.Tables.{initializeState,onTermination}

object ApplicationMain extends App {
  import com.sandinh.paho.akka._
  import scala.concurrent.duration._
  
  private[this] val logger = org.log4s.getLogger
  
  val system = ActorSystem("MQTTActorSystem")
  
  val mqttPubSub = system.actorOf(Props(classOf[MqttPubSub], PSConfig(
      brokerUrl = "tcp://pi2b.local:1883",
      userName = null,
      password = null,
      stashTimeToLive = 1.minute,
      stashCapacity = 8000,
      reconnectDelayMin = 10.millis,
      reconnectDelayMax = 30.seconds
  )))
  
  val mqttSubActor = system.actorOf(SubscribeActor.props, "subActor")
  logger.info("Created mqttSubActor")
  
  Await.result(initializeState, Duration.Inf)
  
  system.registerOnTermination(onTermination)

}