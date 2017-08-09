## Weblogic JMS Monitoring Tool

This project is pure Java SE that can be used as a starting point for a full web application (with Spring, Angular) that manages the JMS Messages within Weblogic Queues using **JMX**.

## Motivation

JMS is part of any big enterprise application and when there is JMS there needs to be Administration as well. 
When the JMS fails after a configured number of retries the JMS Message will end up in the DLQ, with this tool you can see all messages, along with the JMS Headers and Payload, move messages from a Queue to another (usually from Queue1_DLQ to Queue1) or remove them.

## Description

It can connect **remotely** either directly to a Weblogic **Managed Server** or to a Weblogic **Admin Server** from which it can read data from all its Managed Servers.

## Installation

The dependencies are kept at a minimum, most important ones are 5 jars from Weblogic (see pom.xml).
The Jar can be build with Maven. Tested with Java 7.

mvn install

To run the application you need to Run the **Main** class with some properties:

-Dhost=localhost
-Dport=7001
-Duser=weblogic
-Dpass=Password1
-DisAdmin=true // if you connect to AdminServer
-Dprotocol=iiop // optional, defaults to t3

## API Reference

There are 5 operations

1. Get all destinations on all servers: with a messages count on each
2. Count messages on a destination
3. Find messages on a destination: with pagination with page size maximum 5000 & JMS Selector, brings just JMS Headers (no Payload)
4. Read payload of a message: if Payload is XML it will pretty print it, if Serialized it would be a String Representation which might look ugly
5. Move messages from a destination to another filtered by JMS Selector, any number of messages and works fast enough for millions of rows, move works only within same JMS Module
6. Remove messages, same as above, but removes instead of move.

## TODO

- this is a POC that can a starting point for a Spring Application and expose JSON API and AngularJS UI
- while creating the Spring App, make sure you put **@Transactional** so that 2 different clients won't bother each other
- also take care of Thread Safety if you see some areas where there might be problems
- when working directly with ManagedServers (instead of via AdminServer) you can change the code to work with a configurable LIST of SERVERS that you can read from the database instead of 1 single server
- create Unit & Integration Tests
- implement/test **Topics** as well (with **durable subscribers**)
- test in **Cluster Environment with Distributed Queues** (should work out of the box)
- in Development Weblogic has loose security constraints, in **Production** maybe you need to forbid access to JMX, except the specific MBeans and Operations needed by this JMS Monitoring Tool, you can configure **Policies** for that
- you can extend the functionality to **WildFly** and other servers as well!
- when deployed within the Weblogic, the **5 weblogic JARS** don't need to and shouldn't be packed with the application
- when deployed within the Weblogic, via JNDI you could take the JMX Connection details: host, port, servername (except user/pass): not implemented yet. So you don't need to provide the URL

## Tests

Not yet written.

## Contributors

Cosmin

## License

Free to use, change, etc.
