package com.murraywilliams.mqtt

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import java.sql.Date

class MeasurementTable(tag: Tag) extends Table[(String, String, Long, Double)](tag, "sensor") {
  
  def deviceID: Rep[String] = column[String]("dev_id")
  def measurementType: Rep[String] = column[String]("measure")
  def timestamp: Rep[Long] = column[Long]("timestamp")
  def reading: Rep[Double] = column[Double]("reading")
  
  def * : ProvenShape[(String, String, Long, Double)] = (deviceID, measurementType, timestamp, reading)
  def pk = primaryKey("pk", (deviceID, measurementType, timestamp))
}

object Tables {
  import com.typesafe.config.ConfigFactory
  import slick.backend.DatabaseConfig
  import slick.driver.JdbcProfile
  import slick.jdbc.meta.MTable
  import slick.dbio.SuccessAction
  import scala.concurrent.ExecutionContext.Implicits.global
  
  val dsName = ConfigFactory.load.getString("mqtt.dsName")
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile](dsName)
  val db = dbConfig.db
  
  import dbConfig.driver.api._
  
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
  
  val createIfNotExist: DBIO[Unit] = tablesExist.flatMap(exist => if (!exist) create else SuccessAction{})
 
  def initializeState = db.run(createIfNotExist >> addRecord)
  
  def onTermination = { db.close }
}