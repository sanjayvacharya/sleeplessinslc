# Overview

This project is to demonstrate the use of Open API 3.0 for API first development and how a service and service client can be generated using the API definition file.

# Structure of Project
- **notes-api**: This maven module houses the notes api specification under src/main/resources/api.yaml. Ideally this would be made available via some api repository but for the sake of this project demonstration it is being placed in a module where it is shared. Note that the 'classes' are not generated from the api and only the api specification is present in the jar.

- **notes-service**: The service maven module. This uses the api.yaml file in the classpath as defined in the notes-api project to generate the server side Spring MVC stubs. The service implements basic auth using Spring security for demonstration and using Spring Data JPA to service the data. As part of the build process a docker image is created using the spotify docker maven plugin.

- **notes-client**: The client module is for demonstration purposes only. One would typically not create a distributable client jar for reasons you can view in the blog: 

- **notes-integration**: This integration modules demonstrates the invocation of the notes service which runs in a docker container and is invoked using the notes-client.It uses the TestContainers framework to launch the docker image of the generated notes-service prior to runnin tests on it.

# Getting Started

You will need to install Docker and have it running locally as the project will be building docker images for the integration test.

From the command line execute:

```
mvn install
```

The above should result in the service and client stubs getting generated and the integration running.