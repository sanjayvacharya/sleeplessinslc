package com.welflex.order.rest.resource;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.dozer.util.mapping.MapperIF;

import com.sun.jersey.spi.resource.PerRequest;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.model.LineItem;
import com.welflex.model.Order;
import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.LineItemListDto;
import com.welflex.order.dto.OrderDto;
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

  @Autowired
  private MapperIF beanMapper;

  public OrderResource() {}

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  @GET
  @Produces("application/xml")
  public Response getOrder(@Context HttpHeaders hh, @Context Request request) throws OrderNotFoundException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Order Resource Retrieving order:" + orderId);
    }

    Order order = orderService.getOrder(orderId);

    LOG.debug("Checking if there an Etag and whether there is a change in the order...");
    EntityTag etag = computeEtagForOrder(order);
    Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(etag);

    if (responseBuilder != null) {
      // Etag match
      LOG.debug("Order has not changed..returning unmodified response code");
      return responseBuilder.build();
    }

    LOG.debug("Returning full Order to the Client");
    OrderDto orderDto = (OrderDto) beanMapper.map(order, OrderDto.class);

    // Setup line Item
    for (LineItemDto lineItemDto : orderDto.getLineItems()) {
      URI uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path("/products").path("" + lineItemDto.getItemId())
        .build();
      lineItemDto.setProductUri(uri);
    }

    responseBuilder = Response.ok(orderDto).tag(etag);

    return responseBuilder.build();
  }

  private EntityTag computeEtagForOrder(Order order) {
    return new EntityTag("" + order.getLastUpdateDate().getTime());
  }

  @Consumes("application/xml")
  @PUT
  public void updateOrder(@PathParam("id") String id, OrderDto orderDto) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter Update Order, Id=" + id);
    }
    orderDto.setOrderId(orderId);
    Order order = (Order) beanMapper.map(orderDto, Order.class);
    order.setLastUpdateDate(new Date());
    orderService.persist(order);
  }

  @DELETE
  public void deleteOrder(@PathParam("id") String id) {
    orderService.delete(orderId);
  }

  @Path("/lineItems")
  public LineItemListDto getLineItems() throws OrderNotFoundException {
    Order order = orderService.getOrder(orderId);
    List<LineItem> lineItems = order.getLineItems();
    Set<LineItemDto> lineItemDtos = new HashSet<LineItemDto>();

    for (Iterator<LineItem> i = lineItems.iterator(); i.hasNext();) {
      lineItemDtos.add((LineItemDto) beanMapper.map(i.next(), LineItemDto.class));
    }

    return new LineItemListDto(lineItemDtos);
  }
}
