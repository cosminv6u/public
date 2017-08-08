Weblogic JMS Monitoring Tool

This is a plain java application that connects remotely to Weblogic (AdminServer / ManagedNode) via JMX and manages the JMS Queues.
There are 5 system parameters that this tool waits:
	-Dhost=localhost
	-Dport=7001
	-Duser=weblogic
	-Dpass=Password1
	-DisAdmin=true // if you connect to AdminServer (and get info from all ManagedServers) or directly to a specific ManagedServer
	-Dprotocol=iiop // optional, defaults to t3
Operations:
- get all destinations on all servers (with a count messages on each)
- count messages on a destination
- find messages on a destination (with pagination with page size maximum 5000 & JMS Selector)
- read payload of a message (previously only the JMS Headers were brought): if XML it will pretty print it, if Serialized it would be a String Representation (which might look ugly)
- move/remove messages from a destination filtered by JMS Selector, any number of messages (and works fast enough for millions of rows), move works only within same JMS Module

TODO:
- this is a POC that can a starting point for a Spring Application and expose JSON API and AngularJS UI
- while creating the Spring App, make sure you put @Transactional so that 2 different clients won't bother each other
- also take care of Thread Safety if you see some areas where there might be problems
- when working directly with ManagedServers (instead of via AdminServer) you can change the code to work with a configurable LIST of SERVERS, that you can read from the database
- create UnitTests
- implement/test Topics as well (with durable subscribers)
- test in Cluster Environment with Distributed Queues (should work out of the box)
- in Development Weblogic has loose security constraints, in Production maybe you need to forbid access to JMX, except the specific MBeans and Operations needed by this JMS Monitoring Tool
- you can extend the functionality to WildFly and other servers as well!
- when deployed within the Weblogic, the 5 weblogic JARS don't need to and shouldn't be packed with the application
- when deployed within the Weblogic, via JNDI you can take the JMX Connection details: host, port, servername (except user/pass): not implemented yet