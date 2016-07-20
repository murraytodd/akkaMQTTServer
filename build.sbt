name := """akka-mqtt"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.sandinh" %% "paho-akka" % "1.3.0",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.h2database" % "h2" % "1.4.187" % "test",
  "org.log4s" %% "log4s" % "1.3.0",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.7" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test")


fork in run := true