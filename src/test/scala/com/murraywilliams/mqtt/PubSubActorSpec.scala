package com.murrraywilliams.mqtt

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender }
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
 
class MQTTActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  def this() = this(ActorSystem("MySpec"))
 
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
 
  "SubscribeActor" must {
    "receive a message" in {
      //val pingActor = system.actorOf(PingActor.props)
      //pingActor ! PongActor.PongMessage("pong")
      //expectMsg(PingActor.PingMessage("ping"))
    }
  }

  "Publish" must {
    "be able to send a message" in {
      //val pongActor = system.actorOf(PongActor.props)
      //pongActor ! PingActor.PingMessage("ping")
      //expectMsg(PongActor.PongMessage("pong"))
    }
  }

}
