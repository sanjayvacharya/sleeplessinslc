package com.welflex.product.dto;

import java.util.List;

public class BaseProduct {
  private Long productId;

  private String description;

  private String imageUrl;

  private List<BaseProductOption> options;

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<BaseProductOption> getOptions() {
    return options;
  }

  public void setOptions(List<BaseProductOption> options) {
    this.options = options;
  }
}
