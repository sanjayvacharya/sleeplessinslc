package com.welflex.order.rest.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sf.dozer.util.mapping.MapperIF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.core.ResourceContext;
import com.welflex.model.Order;
import com.welflex.order.dto.OrderDto;
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

  @Produces("application/xml")
  @Consumes("application/xml")
  @POST
  public OrderDto storeOrder(OrderDto orderDTO) {

    Order order = (Order) beanMapper.map(orderDTO, Order.class);
    order.setLastUpdateDate(new Date());
    orderService.persist(order);

    OrderDto orderResultDTO = (OrderDto) beanMapper.map(order, OrderDto.class);

    return orderResultDTO;
  }

  public void validate() {
    Assert.notNull(orderService);
  }
}
