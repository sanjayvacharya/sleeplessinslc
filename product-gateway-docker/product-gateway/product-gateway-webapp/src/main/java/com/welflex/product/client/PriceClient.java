package com.welflex.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.overstock.price.dto.ProductPrice;

@FeignClient("price")
public interface PriceClient {
  @RequestMapping(method = RequestMethod.GET, value = "/productPrice/{id}", consumes = "application/json")
  ProductPrice getPrice(@PathVariable("id") Long id);
}
