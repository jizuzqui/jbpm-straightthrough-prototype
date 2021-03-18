# jbpm-straightthrough-prototype

This app can be packaged and deployed as a war file in EAP 7.3.

- It currently allows starting a process by sending the process definition (processDefinitionId). A new process instance for this process definition will be starte. Example:
    ````
    POST http://localhost:8080/services/rest/business-process-management/v1/process-models/{processId}/versions/{version}:instance
    ````
- The api currently does not accept input parameters 
- Tests are WIP (Arquillian is not enabled yet)

**Package and deploy**

There is a Makefile to build the app and the docker image.

```sh

make run_local # for runing in local

make test # for testing

make build # for building the app

make run # for runing the docker image

make image # for making the image globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:latest

make push # for pushing to bbva private registry


```


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



