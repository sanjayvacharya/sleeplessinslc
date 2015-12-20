package com.welflex.client;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.welflex.exception.OrderException;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.order.dto.OrderDto;

/**
 * A Client Wrapper around Web service Calls.
 *
 * @author Sanjay Acharya
 */
@Consumes("application/xml")
public interface OrderClient {
  
  /**
   * Create an Order.
   *
   * @param orderDTO Order DTO
   * @return OrderDTO with created id
   * @throws IOException If an error occurs
   */
  @POST
  @Path("/orders")
  @Produces("application/xml")
  @Consumes("application/xml")
  public OrderDto createOrder(OrderDto orderDTO) throws IOException;

  /**
   * Updates an Order.
   *
   * @param orderDTO Order DTO
   */
  @PUT
  @Path("/orders/{id}")
  @Produces("application/xml")
  public void updateOrder(OrderDto orderDto, @PathParam ("id") Long id);

  /**
   * Retrieves an Order with the specified <code>orderId</code>.
   *
   * @param orderId Order Id
   * @return OrderDto
   * @throws OrderNotFoundException if order is not found
   * @throws IOException if an error occurs
   */
  @GET
  @Path("/orders/{id}")
  @Produces("application/xml")
  public OrderDto getOrder(@PathParam("id") Long orderId) throws OrderNotFoundException, IOException;

  /**
   * Deletes an Order with the specified <code>orderId</code>
   *
   * @param orderId order Id
   * @throws OrderException If an error occurs
   */
  @DELETE
  @Path("/orders/{id}")
  @Produces("application/xml")
  public void deleteOrder(@PathParam("id") Long orderId) throws OrderException;
}
