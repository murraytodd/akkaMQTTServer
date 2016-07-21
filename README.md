# akkaMQTTServer
An experimental &amp; learning exercise in applying the following standards
and technologies:

* MQTT - The simple and lightweight messaging protocol used in IoT
* Akka - A lightweight Scala (and Java) framework that brings the concept of Reactive programming to microservices
* Slick - An asynchronous Scala persistence framework designed by the same people (Lightbend, formerly Typesafe) that brought us Scala and Akka

In my example that uses this server, I will also be touching these trendy bits:

* Raspberry Pi - Not really a technology, but a cool &amp; inexpensive way to create a  service that can be part of a larger IoT infrastructure. In my example, I'm going to use a Raspberry Pi to act as my server, hosting a (Mosquitto) MQTT server, a PostgreSQL database, and this Akka+Slick+MQTT microservice.
* Arduino - It's hard to articulate what an Arduino is if you don't already know. It's the combination of a super-inexpensive microcontroller ($10 for an Arduino Pro Mini) and a community-backed open source framework that makes it easy to create, wire and program devices.
* XBee - A wireless "mesh network" radio network device that can be used to connect small devices (like Arduinos connected to sensors and/or my Raspberry Pi)



 

