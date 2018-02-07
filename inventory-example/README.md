# Overview

This is a sample Sellable Inventory system used to demonstrate Kafka Streams and CQRS. Neha Narkhede discusses an Inventory System that 
uses Event Sourcing, CQRS and Kafka Streams in her Blog https://www.confluent.io/blog/event-sourcing-cqrs-stream-processing-apache-kafka-whats-connection/.

This repository has the necessary code to run the example demonstrated by her while also showing how it would work.

# Getting Started

# Structure
Module | Purpose
--- | ---
`inventory-mutator` | Has simple API's for accepting inventory adjustments and reservations and represents the command side of CQRS.
`sellable-inventory-calculator` | This is a stream processor that computes sellable inventory. It is also a service that has an API to query for sellable inventory. 
`sellable-inventory-service` | This is the Query side of CQRS where one can query the sellable inventory. The sellable inventory service stores the sellable inventory it receives from the sellable-inventory-calculator in Cassandra.
`schema` | Avro schema definition and Kafka Topics (Shared library)
`docker` | Contains docker-compose files to start artifacts like datastore/kafka etc
`integration-test` | Has a test that demonstrates the entire example

# Pre-Requisities
* JDK 1.8
* Maven 3.3.x
* Docker
* Docker Compose

#  Building
`mvn clean install`

# Running Services

Execute the following command to set the DOCKER_KAFKA_HOST variable. 'export DOCKER_KAFKA_HOST=$(ipconfig getifaddr en0)'. 
From the docker folder issue `docker-compose -f docker-compose-dev.yml up ` to start the artifacts needed to run the example. If you would like to run the 
different Microservices from your IDE, make sure you provide the VM Argument '-Dspring.kafka.bootstrap.servers=${DOCKER_KAFKA_HOST}:9092' ensuring that
DOCKER_KAFKA_HOST translates to what was exported earlier.

If you prefer to instead simply run the entire app in Docker, then after the maven install command and setting of the DOCKER_KAFKA_HOST,
execute the following to start the different components `docker-compose up`. This results in a three Kafka servers starting and two 
instances of the sellable-inventory-calculator starting. The two separate instances allows you to test the forwarding of the request 
from an instance that does not have the {Location, SKU} in its local State Store to an instance that does have it.

Further details about this project can be found at: 
Follow the instructions at https://sleeplessinslc.blogspot.com


