package com.murrraywilliams.mqtt

import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.{Future, Await}
import com.murraywilliams.mqtt.MeasurementTable
import slick.jdbc.meta.MTable
import slick.dbio.SuccessAction
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

object ApplicationMain extends App {
  import com.sandinh.paho.akka._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global
  
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("mqttPSQLds")
  val db = dbConfig.db
  import dbConfig.driver.api._
  
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
  
  val measurements: TableQuery[MeasurementTable] = TableQuery[MeasurementTable]
  
  val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector(measurements.baseTableRow.tableName)
    names.intersect(tables.map(_.name.name)) == names
  }
  
  val create: DBIO[Unit] = DBIO.seq(
      measurements.schema.create
  )
  
  val addRecord: DBIO[Unit] = DBIO.seq(
      measurements += ("arduino","tempK",(new java.util.Date()).getTime, 23.4)
  )
  
  val createIfNotExist: DBIO[Unit] = tablesExist.flatMap(exist => if (!exist) create else SuccessAction{}).flatMap(_ => addRecord)
  
  Await.result(db.run(createIfNotExist), Duration.Inf)
  
  
  db.close
  println("DB closed")
}