package com.welflex.price.service;

import java.util.Map;

public class PricingServiceImpl implements PricingService {
  private final Map<Long, Double> priceMap;

  public PricingServiceImpl(Map<Long, Double> priceMap) {
    this.priceMap = priceMap;
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
