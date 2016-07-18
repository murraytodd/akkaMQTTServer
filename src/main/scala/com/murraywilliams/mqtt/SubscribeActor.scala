package com.murrraywilliams.mqtt

import akka.actor.{Actor,Props}
import com.sandinh.paho.akka.Subscribe
import com.sandinh.paho.akka.SubscribeAck
import com.sandinh.paho.akka.Message

class SubscribeActor extends Actor {
  
  private[this] val logger = org.log4s.getLogger
  val topic = "hello/world"
  
  logger.info("Starting SubScribeActor")
  println("Starting SubScribeActor.")
  ApplicationMain.mqttPubSub ! Subscribe(topic,self)

  def receive  = {
    case SubscribeAck(Subscribe(`topic`, `self`, _), fail) =>
      if (fail.isEmpty) {
        context become ready
        logger.info(s"Subscribed to $topic")
      } else logger.error(fail.get)(s"Can't subscribe to $topic")
  }
  
  def ready: Receive = {
    case msg: Message => logger.info(new String(msg.payload))
  }
}

object SubscribeActor {
  val props = Props[SubscribeActor]
  case object Initialize
}