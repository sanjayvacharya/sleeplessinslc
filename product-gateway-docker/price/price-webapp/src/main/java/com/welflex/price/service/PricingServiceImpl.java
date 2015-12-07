package com.welflex.price.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

@Service
public class PricingServiceImpl implements PricingService {
  private final Map<Long, Double> priceMap;

  public PricingServiceImpl() {
    priceMap = Maps.newHashMap();
    priceMap.put(1L, new Double(39.99));
    priceMap.put(2L, new Double(32.99));
    priceMap.put(3L, new Double(39.99));
    priceMap.put(4L, new Double(39.99));
    priceMap.put(9310301L, new Double(38.99));
  }

  @Override
  public Double price(Long productId) {
    try {
      Thread.sleep(20);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return priceMap.get(productId);
  }
}
