package com.welflex.client;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.welflex.exception.OrderException;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.order.dto.OrderDto;

/**
 * An Implementation of the {@link OrderClient} that delegates all its processing to a RestEasy
 * Proxy client.
 *
 * @author Sanjay Acharya
 */
public class OrderClientImpl implements OrderClient {
  private final WebResource resource;
  private static final Logger LOG = Logger.getLogger(OrderClientImpl.class);

  /**
   * @param uri Server Uri
   */
  public OrderClientImpl(String uri) {
    ClientConfig cc = new DefaultClientConfig();
    // Include the properties provider
    Client delegate = Client.create(cc);
    resource = delegate.resource(uri).path("orders");
  }

  public OrderDto createOrder(OrderDto orderDTO) throws IOException {
    return resource.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML).post(
      OrderDto.class, orderDTO);
  }

  public void deleteOrder(Long orderId) throws OrderException {
    CacheManager.remove(resource.path(orderId.toString()).getURI().getPath());
    resource.path(orderId.toString()).type(MediaType.APPLICATION_XML).delete();
  }

  public OrderDto getOrder(Long orderId) throws OrderNotFoundException, IOException {
    try {
      String path = resource.path(orderId.toString()).getURI().getPath();
      CacheEntry entry = CacheManager.get(path);
      Builder wr = resource.path(orderId.toString()).accept(MediaType.APPLICATION_XML);

      if (entry != null && entry.getEtag() != null) {
        wr.header("If-None-Match", entry.getEtag().getValue());
      }

      ClientResponse response = wr.get(ClientResponse.class);

      if (response.getResponseStatus().equals(Status.NOT_MODIFIED)) {
        LOG.debug("Order has not been modified..returning Cached Order...");
        return (OrderDto) entry.getObject();
      }
      else if (response.getResponseStatus().equals(Status.OK)) {
        LOG.debug("Obtained full Order from Service...Caching it..");
        OrderDto dto = response.getEntity(OrderDto.class);
        CacheManager.cache(path, new CacheEntry(dto, response.getEntityTag(), null));

        return dto;
      }
      else {
        LOG.debug("Order not found on server...removing from cache");
        CacheManager.remove(path);
        throw new UniformInterfaceException(response);
      }
    }
    catch (UniformInterfaceException e) {

      if (e.getResponse().getStatus() == Status.NOT_FOUND.getStatusCode()) {
        throw new OrderNotFoundException(e.getResponse().getEntity(String.class));
      }
      throw new RuntimeException(e);
    }

  }

  public void updateOrder(OrderDto orderDto, Long id) {
    resource.path(id.toString()).type("application/xml").put(orderDto);
  }
}
