# jbpm-straightthrough-prototype

This app can be packaged and deployed as a war file in EAP 7.3.

- It currently allows starting a process by sending the process definition (processDefinitionId). A new process instance for this process definition will be starte. Example:
    ````
    POST http://localhost:8080/jbpmsample/rest/processes/{processId}
    ````
- The api currently does not accept input parameters 
- Tests are WIP (Arquillian is not enabled yet)

**Package and deploy**

`$ mvn clean package -DskipTests && cp target/jbpm-prototype.war $JBOSS_HOME/standalone/deployments/ && touch $JBOSS_HOME/standalone/deployments/jbpmsample.war.dodeploy`


**Tests**

Since the tests are not using arquillian nor mocks, it is necessary to build and deploy the application it to the app server. When you have the application deployed, you can run test both tests: test directly the Java API and test the REST endpoint. 

See below an example of the tests execution, with the execution time for both apis:
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



