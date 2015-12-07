package com.overstock.inventory.model;


public class ProductInventory {  

  private Long productId;
  
  private int count;
  
  public ProductInventory(Long productId, int count) {
    this.productId  = productId;
    this.count = count;
  }

  public Long getProductId() {
    return productId;
  }

  public int getCount() {
    return count;
  }
}
