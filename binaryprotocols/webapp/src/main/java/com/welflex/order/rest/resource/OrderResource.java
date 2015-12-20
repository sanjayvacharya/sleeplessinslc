package com.welflex.order.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.PerRequest;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.model.Order;
import com.welflex.order.rest.util.Utils;
import com.welflex.orders.proto.dto.OrderProtos;
import com.welflex.provider.AlternateMediaType;
import com.welflex.service.OrderService;

@PerRequest
@Component
@Scope("prototype")
public class OrderResource {
  private static final Logger LOG = Logger.getLogger(OrderResource.class);

  private Long orderId;

  @Context
  protected UriInfo uriInfo;

  @Autowired
  private OrderService orderService;
  public OrderResource() {}

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  @GET
  @Produces(AlternateMediaType.APPLICATION_XPROTOBUF)
  public Response getOrder(@PathParam("id") String id) throws OrderNotFoundException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Order Resource Retrieving order:" + orderId);
    }

    try {
      Order order = orderService.getOrder(orderId);  
      return Response.ok(Utils.build(order), AlternateMediaType.APPLICATION_XPROTOBUF).build();
    }
    catch (OrderNotFoundException e) {
      LOG.info("Order Not Found [" + orderId + "]", e);
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Consumes(AlternateMediaType.APPLICATION_XPROTOBUF)
  @PUT
  public void updateOrder(@PathParam("id") String id, OrderProtos.Order orderDto) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Updating Order, Id=" + id);
    }
    
    Order order = Utils.build(orderDto);
    order.setOrderId(Long.parseLong(id));  
    orderService.persist(order);
  }

  @DELETE
  public void deleteOrder(@PathParam("id") String id) {
    orderService.delete(orderId);
  }
}
