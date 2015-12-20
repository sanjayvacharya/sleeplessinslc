package com.welflex.clients;


import org.apache.commons.httpclient.HttpClient;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.welflex.exception.OrderException;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.exception.OrderValidationException;
import com.welflex.orders.proto.dto.OrderProtos;
import com.welflex.provider.AlternateMediaType;
import com.welflex.provider.ProtobufMessageReader;
import com.welflex.provider.ProtobufMessageWriter;

public class OrdersClient {
  private final String baseUri;

  private final ApacheHttpClient client;

  public OrdersClient(String baseUri) {
    this.baseUri = baseUri;
    ClientConfig cc = new DefaultClientConfig();
    cc.getClasses().add(ProtobufMessageReader.class);
    cc.getClasses().add(ProtobufMessageWriter.class);
    ApacheHttpClientHandler handler = new ApacheHttpClientHandler(new HttpClient());
    client = new ApacheHttpClient(handler, cc);   
  }

  private void throwExceptionIfNecessary(ClientResponse response) throws OrderValidationException,
    OrderException {
    if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
      throw new OrderValidationException(response.getEntity(String.class));
    }
    else if (response.getStatus() == Status.SERVICE_UNAVAILABLE.getStatusCode()) {
      throw new OrderValidationException(response.getEntity(String.class));
    }
  }

  public OrderProtos.Order create(OrderProtos.Order dto) throws OrderValidationException,
    OrderException {
    ClientResponse response = null;
    try {
      response = client.resource(baseUri).path("/orders").accept(AlternateMediaType.APPLICATION_XPROTOBUF)
        .type(AlternateMediaType.APPLICATION_XPROTOBUF)
        .entity(dto, AlternateMediaType.APPLICATION_XPROTOBUF)
          .post(ClientResponse.class);
      throwExceptionIfNecessary(response);
      return response.getEntity(OrderProtos.Order.class);
    }
    finally {
      if (response != null) {
        response.close();
      }
    }
  }

  public void update(OrderProtos.Order dto) throws OrderNotFoundException,
    OrderValidationException,
    OrderException {
    ClientResponse response = null;
    try {
      response = client.resource(baseUri).path("/orders").path(String.valueOf(dto.getId())).entity(
        dto, AlternateMediaType.APPLICATION_XPROTOBUF).put(ClientResponse.class);
      throwExceptionIfNecessary(response);
    }
    finally {
      if (response != null) {
        response.close();
      }
    }
  }

  public OrderProtos.Order get(Long orderId) throws OrderNotFoundException, OrderException {
    ClientResponse response = null;
    try {
      response = client.resource(baseUri).path("/orders")
        .path(String.valueOf(orderId)).accept(AlternateMediaType.APPLICATION_XPROTOBUF)
         .get(ClientResponse.class);

      if (response.getStatus() == Status.OK.getStatusCode()) {
        return response.getEntity(OrderProtos.Order.class);
      }
      else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
        throw new OrderNotFoundException(response.getEntity(String.class));
      }
      else if (response.getStatus() == Status.SERVICE_UNAVAILABLE.getStatusCode()) {
        throw new OrderException(response.getEntity(String.class));
      }
      throw new OrderException("Unexpected");
    }
    finally {
      if (response != null) {
        response.close();
      }
    }
  }

  public void delete(Long orderId) throws OrderException {
    client.resource(baseUri).path("/orders").path(String.valueOf(orderId)).delete(
      ClientResponse.class);
  }
}
