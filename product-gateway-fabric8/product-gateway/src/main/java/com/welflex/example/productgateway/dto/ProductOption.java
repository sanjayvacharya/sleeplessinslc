package com.welflex.example.productgateway.dto;

public class ProductOption {
  private Long productId;

  private String optionDescription;

  private int inventory;

  private Double price;

  public ProductOption() {}

  public ProductOption(Long productId, String optionDescription) {
    this.productId = productId;
    this.optionDescription = optionDescription;
  }

  public int getInventory() {
    return inventory;
  }

  public Double getPrice() {
    return price;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getOptionDescription() {
    return optionDescription;
  }

  public void setOptionDescription(String optionDescription) {
    this.optionDescription = optionDescription;
  }

  public void setInventory(int inventory) {
    this.inventory = inventory;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
}
