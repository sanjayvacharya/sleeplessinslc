package com.welflex.price.dto;


public class ProductPrice {
  private Long productId;

  private Double price;

  public ProductPrice() {}

  public ProductPrice(Long productId, Double price) {
    this.productId = productId;
    this.price = price;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

}
