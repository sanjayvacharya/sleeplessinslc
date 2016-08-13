package com.welflex.product.model;

import java.util.List;

public class Product {
  private Long productId;

  private String description;

  private String imageUrl;

  private List<ProductOption> options;

  public Product(Long productId, String description, String imageUrl, List<ProductOption> options) {
    this.productId = productId;
    this.description = description;
    this.imageUrl = imageUrl;
    this.options = options;
  }

  public Long getProductId() {
    return productId;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public List<ProductOption> getOptions() {
    return options;
  }
}
