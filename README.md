# akkaMQTTServer
An experimental &amp; learning exercise in applying the following standards
and technologies:

* [MQTT](http://mqtt.org) - The simple and lightweight messaging protocol used in IoT. ([Here's](http://www.hivemq.com/blog/mqtt-essentials/) a good set of beginning articles on MQTT Essentials)
* [Akka](http://akka.io) - A lightweight Scala (and Java) framework that brings the concept of [Reactive](http://www.reactivemanifesto.org) programming to microservices
* [Slick](http://slick.lightbend.com) - An asynchronous Scala persistence framework designed by the same people (Lightbend, formerly Typesafe) that brought us Scala and Akka

In my example that uses this server, I will also be touching these trendy bits:

* [Raspberry Pi](https://www.raspberrypi.org) - Not really a technology, but a cool &amp; inexpensive way to create a  service that can be part of a larger IoT infrastructure. In my example, I'm going to use a Raspberry Pi to act as my server, hosting a (Mosquitto) MQTT server, a PostgreSQL database, and this Akka+Slick+MQTT microservice.
* [Arduino](https://ww.arduino.cc) - It's hard to articulate what an Arduino is if you don't already know. It's the combination of a super-inexpensive microcontroller ($10 for an Arduino Pro Mini) and a community-backed open source framework that makes it easy to create, wire and program devices
* [XBee](https://www.arduino.cc) - A wireless "mesh network" radio network device that can be used to connect small devices (like Arduinos connected to sensors and/or my Raspberry Pi) using the [ZigBee](http://www.zigbee.org) standard

## Functional goal of the server

This Akka service listens to an existing MQTT broker for  sensor reading notifications that come from some IoT devices and records the sensor data into a database. The functionality is generalized so that any device can send any sorts of measurements (pressure, temperature, etc.) as long as those measurements can be represented by a floating-point (double) value.

As a special bit of functionality, the timestamps for these measurements can be represented one of three ways:

1. The device can specify the measurement's timestamp. (Not often practical since microcontrollers often lack clocks!)
2. The server can use the timestamp that the message was received. (I.e. "Now")
3. If a series of sequential measurements is sent, the server will assume the measurements are being taken at regular intervals and sent "in batch". It will look at the time that the last batch had been sent and approximate the timestamp of each element in the sequence accordingly.

### Q: What's up with this funny sequence of measurements?

Imagine you have wired-up an Arduino to a sensor, and it's depending on using an XBee radio to transmit the measurements to your server. Furthermore, your Arduino is out in the field somewhere without a power source, so you're using a small solar panel and a rechargeable battery.

This contraption has to record and send data overnight, when the solar panel isn't providing juice, so you're depending on the battery and making your power consumption _really_ lean in order to make it through the night.

I had a blast figuring out how to do this, and it involved putting the Arduino to sleep, waking up every couple minutes for a sensor reading, saving 10-20 readings at a time and then finally powering the XBee radio (a _serious_ energy drain) just long enough to send out those 10-20 readings before shutting it down again.

The Arduino doesn't have any reliable clock, especially when it's in deep sleep mode, so my server has to extrapolate and figure out what 20 evenly spaced timestamps would be between the current data bundle and the last one.

### Q: Isn't this problem a little too simple for Akka?

I'm not sure yet. It's actually a funny question because Akka is considered to be one of the simplest service frameworks. (Play, which I considered using, is built on top of it.) I actually think that this server might evolve over time to be able to respond to MQTT and/or REST requests for the most recent measurements, the measurements over the past day, etc. I may also want to try to program a weather forecasting module that uses the ongoing trends in temperature/pressure/humidity changes to predict the weather, and that may actually be a good event-based exercise, so we'll see.

## Arduino sensor code

The `/arduino` directory has a single sketch that I created to take temperature
measurements with the MCP9808 temperature sensor and sending the results via an
XBee Series 2 device (leveraging the [Adafruit library](https://github.com/adafruit/Adafruit_MCP9808_Library) for the sensor and [Andrew Rapp's library](https://github.com/andrewrapp/xbee-arduino) for the XBee).

At the moment, we don't have enough pieces to put everything together yet. We still
need to write the code that will run on the Raspberry Pi to receive the Arduino
sensor readings and send them to the MQTT server. More information can be found on
the [README.md](arduino/README.md) file in the Arduino directory.