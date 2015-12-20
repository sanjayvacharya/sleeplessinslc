package com.welflex.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.welflex.order.dto.ProductListDto;

/**
 * Wrapper around Product calls.
 *
 * @author Sanjay Acharya
 */
@Consumes("application/json")
public interface ProductClient {
  /**
   * @return A ProductListDto that represents the Products supported.
   */
  @GET
  @Path("/products")
  public ProductListDto getProducts();
}
