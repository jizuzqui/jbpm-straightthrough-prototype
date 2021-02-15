# jbpm-straightthrough-prototype

This app can be packaged and deployed as a war file in EAP 7.3.

- It can currently start a process by sending the process definition (processId) to started as a urlparam. Example:
POST http://localhost:8080/jbpmsample/rest/processes/{processId}
- Currently not accepting parameters as input
- Tests are WIP (doesn't have arquillian yet)



**Tests**

The tests are currently using arquillian yet, so it's necessary to build and deploy it to a server if you want to test both tests: with java api and with rest api. 

````
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.sample.ProcessEndpointTest
 ==
 == Execution time, REST API:9216
 ==
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.42 sec
Running com.sample.StraightThroughTest
 ... 
 ==
 == Execution time, Java API:9161
 ==
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.759 sec
````

**Package and deploy**

`$ mvn clean package -DskipTests && cp target/jbpmsample.war $JBOSS_HOME/standalone/deployments/ && touch $JBOSS_HOME/standalone/deployments/jbpmsample.war.dodeploy`


