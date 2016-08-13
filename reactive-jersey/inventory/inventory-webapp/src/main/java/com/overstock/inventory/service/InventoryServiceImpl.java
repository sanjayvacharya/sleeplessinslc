package com.overstock.inventory.service;

import java.util.Map;

import com.overstock.inventory.model.ProductInventory;

public class InventoryServiceImpl implements InventoryService {
  private Map<Long, ProductInventory> inventoryMap;
  
  public InventoryServiceImpl(Map<Long, ProductInventory> productInventoryMap) {
    this.inventoryMap = productInventoryMap;
  }
  
  @Override
  public ProductInventory getInventory(Long productId) {
    try {
      Thread.sleep(30);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return inventoryMap.get(productId);
  }
}
