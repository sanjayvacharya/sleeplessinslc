package com.welflex.order.rest.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.dozer.util.mapping.MapperIF;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.core.ResourceContext;


import com.welflex.exception.OrderException;
import com.welflex.exception.OrderValidationException;
import com.welflex.model.Order;
import com.welflex.order.dto.OrderDto;
import com.welflex.order.rest.util.Utils;
import com.welflex.orders.proto.dto.OrderProtos;
import com.welflex.provider.AlternateMediaType;
import com.welflex.service.OrderService;

/**
 * OrderResource. Delegates the processing to an Order Service implementation. Maintainence: I had
 * to use the @Component tag here. Should not be necessary as per the documentation.
 *
 * @author Sanjay Acharya
 */
@Path("/orders")
@Component
@Scope("singleton")
public class OrdersResource {
  private static final Logger LOG = Logger.getLogger(OrdersResource.class);
  @Context
  private ResourceContext resourceContext;

  @Autowired
  private OrderService orderService;

  @Autowired
  private MapperIF beanMapper;

  public OrdersResource() {
    super();
  }

  @Path("/{id}")
  public OrderResource subResource(@PathParam("id") String id) {
    final OrderResource resource = resourceContext.getResource(OrderResource.class);
    resource.setOrderId(Long.parseLong(id));

    return resource;
  }

  @Produces(AlternateMediaType.APPLICATION_XPROTOBUF)
  @Consumes(AlternateMediaType.APPLICATION_XPROTOBUF)
  @POST
  public Response store(OrderProtos.Order orderDto) {
    LOG.info("Creating the Order..:" + orderDto);
    Order order = Utils.build(orderDto);
    try {   
      orderService.persist(order);
      OrderProtos.Order resultDto = Utils.build(order);
      LOG.info("Order Result of POST:" + resultDto);
      return Response.ok(Utils.build(order)).type(AlternateMediaType.APPLICATION_XPROTOBUF).build();
    } catch (OrderValidationException e) {
      LOG.info("Order Validation Exception", e);
      return Response.status(Status.BAD_REQUEST).build();      
    } catch (OrderException e) {
      LOG.info("Order Exception", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Produces("application/xml")
  @Consumes("application/xml")
  @POST
  public Response storeOrder(OrderDto orderDTO) {

    Order order = (Order) beanMapper.map(orderDTO, Order.class);
    try {   
      orderService.persist(order);
      OrderDto orderResultDTO = (OrderDto) beanMapper.map(order, OrderDto.class);

      return Response.ok(orderResultDTO).build();

    } catch (OrderValidationException e) {
      return Response.status(Status.BAD_REQUEST).build();      
    } catch (OrderException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  public void validate() {
    Assert.notNull(orderService);
  }
}
