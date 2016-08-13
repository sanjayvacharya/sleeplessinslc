package com.overstock.inventory;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.overstock.inventory.model.ProductInventory;
import com.overstock.inventory.service.InventoryService;
import com.overstock.inventory.service.InventoryServiceImpl;

@Configuration
public class InventoryJavaConfig {
  @Bean
  public InventoryService inventoryService() {
    Map<Long, ProductInventory> productInventory = Maps.newHashMap();
    productInventory.put(1L, new ProductInventory(1L, 20));
    productInventory.put(2L, new ProductInventory(2L, 32));
    productInventory.put(3L, new ProductInventory(3L, 41));
    productInventory.put(4L, new ProductInventory(4L, 0));
    productInventory.put(9310301L, new ProductInventory(9310301L, 20 + 32 + 41));
    
    InventoryServiceImpl service = new InventoryServiceImpl(productInventory);
    
    return service;
  }
}
