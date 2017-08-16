package com.welflex.example.baseproduct.dto;

public class BaseProductOption {
  private Long productId;

  private String optionDescription;

  public BaseProductOption() {
  }

  public BaseProductOption(Long productId, String optionDescription) {
	this.productId = productId;
	this.optionDescription = optionDescription;
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
}
