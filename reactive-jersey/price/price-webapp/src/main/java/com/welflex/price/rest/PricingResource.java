package com.welflex.price.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.overstock.price.dto.ProductPrice;
import com.welflex.price.service.PricingService;

@Path("productPrice/{productId}")
public class PricingResource {
  private final PricingService pricingService;

  @Inject
  public PricingResource(PricingService pricingService) {
    this.pricingService = pricingService;
  }

  @GET
  public ProductPrice price(@PathParam("productId") Long productId) {
    return new ProductPrice(productId, pricingService.price(productId));
  }
}
