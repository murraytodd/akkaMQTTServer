package com.example

import akka.actor.ActorSystem
import akka.actor.Props

object ApplicationMain extends App {
  import com.sandinh.paho.akka._
  import scala.concurrent.duration._
  
  private[this] val logger = org.log4s.getLogger
  
  val system = ActorSystem("MQTTActorSystem")
  val pingActor = system.actorOf(PingActor.props, "pingActor")
  
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
  
  pingActor ! PingActor.Initialize
  // This example app will ping pong 3 times and thereafter terminate the ActorSystem - 
  // see counter logic in PingActor
  system.awaitTermination()
}