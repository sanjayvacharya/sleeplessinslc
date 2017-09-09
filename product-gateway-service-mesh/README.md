## Overview

This project is used to demonstrate Istio and Linkerd service meshes with GRPC and Spring Boot services.

The documentation behind this blog is available at:
https://sleeplessinslc.blogspot.com/2017/09/service-mesh-examples-of-istio-and.html

## Points of Note

- Demonstration of same product-gateway across linkerd and istio
- Use of Spring Boot and Protocol Buffers
- Composition of protocol buffer schemas
- Creation of Docker images for the Microservices
- Product Gateway exposes a HTML and JSON representation
- The application only supports ONE Product at the resource /product/9310301
- The applications only support happy path!

## Structure of Project

- baseproduct: Location of Base product microservice
- inventory: Location of Inventory microservice
- price: Location of price microservice
- reviews: Location of Reviews microservice
- product-gateway: Location of Product Gateway microservice
- linkerd: Folder containing Kubernetes deployment script for linkerd
- istio: Folder containing Kubernetes deployment script for linkerd

# Getting Started

You can run this project by importing into Eclipse and starting the individual services. The below will create Docker images as well.
Building Maven project:
```bash
mvn clean install
```
# Running in Kubernetes

Follow the instructions at https://sleeplessinslc.blogspot.com/2017/09/service-mesh-examples-of-istio-and.html for more details
