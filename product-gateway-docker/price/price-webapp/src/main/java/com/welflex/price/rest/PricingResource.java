package com.welflex.price.rest;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.overstock.price.dto.ProductPrice;
import com.welflex.price.service.PricingService;

@RestController
public class PricingResource {
  private final PricingService pricingService;

  @Inject
  public PricingResource(PricingService pricingService) {
    this.pricingService = pricingService;
  }


  @RequestMapping(value = "/productPrice/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductPrice price(@PathVariable("productId") Long productId) {
    return new ProductPrice(productId, pricingService.price(productId));
  }
}
