package com.welflex.price;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.welflex.price.service.PricingService;
import com.welflex.price.service.PricingServiceImpl;

@Configuration
public class PricingJavaConfig {
  @Bean
  public PricingService pricingService() {
    Map<Long, Double> pricingOfInventory = Maps.newHashMap();
    pricingOfInventory.put(1L, new Double(39.99));
    pricingOfInventory.put(2L, new Double(32.99));
    pricingOfInventory.put(3L, new Double(39.99));
    pricingOfInventory.put(4L, new Double(39.99));
    pricingOfInventory.put(9310301L, new Double(38.99));
    return new PricingServiceImpl(pricingOfInventory);
  }
}
