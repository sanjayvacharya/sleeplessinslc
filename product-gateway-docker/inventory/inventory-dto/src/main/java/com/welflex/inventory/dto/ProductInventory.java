package com.welflex.inventory.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductInventory {
  private Long productId;

  private int count;

  public ProductInventory() {}

  public ProductInventory(Long productId, int count) {
    this.productId = productId;
    this.count = count;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
