package com.overstock.inventory.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.overstock.inventory.model.ProductInventory;

@Service
public class InventoryServiceImpl implements InventoryService {
  private Map<Long, ProductInventory> productInventory;
  
  public InventoryServiceImpl() {
    this.productInventory = Maps.newHashMap();
    productInventory.put(1L, new ProductInventory(1L, 20));
    productInventory.put(2L, new ProductInventory(2L, 32));
    productInventory.put(3L, new ProductInventory(3L, 41));
    productInventory.put(4L, new ProductInventory(4L, 0));
    productInventory.put(9310301L, new ProductInventory(9310301L, 20 + 32 + 41));
  }
  
  @Override
  public ProductInventory getInventory(Long productId) {
    try {
      Thread.sleep(30);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return productInventory.get(productId);
  }
}
