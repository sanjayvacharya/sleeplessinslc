package com.welflex.order.rest.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.welflex.exception.OrderNotFoundException;

@Provider
@Component
public class OrderNotFoundProvider implements ExceptionMapper<OrderNotFoundException> {
  private static final Logger log = Logger.getLogger(OrderNotFoundProvider.class);
  public Response toResponse(OrderNotFoundException exception) {
    log.debug("Enter toResponse() of OrderNotProvider..");
    
    return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage())
      .type("text/plain").build();
  }
}
