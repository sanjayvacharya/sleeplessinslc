package com.welflex.example.price.rest;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

@RestController
public class PricingResource {
  private static final Map<Long, Double> priceMap = Maps.newHashMap();
  static {
	priceMap.put(1L, new Double(39.99));
	priceMap.put(2L, new Double(32.99));
	priceMap.put(3L, new Double(39.99));
	priceMap.put(4L, new Double(39.99));
	priceMap.put(9310301L, new Double(38.99));
  }

  @RequestMapping(value = "/productPrice/{productId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public String price(@PathVariable("productId") Long productId) {
	return String.valueOf(priceMap.get(productId));
  }
}
