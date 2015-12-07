package com.overstock.inventory.service;

import com.overstock.inventory.model.ProductInventory;

public interface InventoryService {
  public ProductInventory getInventory(Long productId);
}
