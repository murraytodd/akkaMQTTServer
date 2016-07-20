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