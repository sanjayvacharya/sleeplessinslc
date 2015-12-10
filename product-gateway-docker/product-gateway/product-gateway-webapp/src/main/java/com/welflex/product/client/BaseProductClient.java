package com.welflex.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.welflex.product.dto.BaseProduct;

@FeignClient("baseproduct")
public interface BaseProductClient {
  @RequestMapping(method = RequestMethod.GET, value = "/baseProduct/{id}", consumes = "application/json")
  BaseProduct getBaseProduct(@PathVariable("id") Long id);
}
