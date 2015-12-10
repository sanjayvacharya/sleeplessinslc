package com.welflex.inventory.service;

import com.welflex.inventory.model.ProductInventory;

public interface InventoryService {
  public ProductInventory getInventory(Long productId);
}
