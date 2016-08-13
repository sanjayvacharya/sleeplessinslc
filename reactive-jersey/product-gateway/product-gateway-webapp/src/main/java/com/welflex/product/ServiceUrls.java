package com.welflex.product;

public class ServiceUrls {
  private final String baseProductUrl;

  private final String priceUrl;

  private final String inventoryUrl;

  private final String reviewsUrl;

  public ServiceUrls(String baseProductUrl, String priceUrl, String inventoryUrl, String reviewsUrl) {
    this.baseProductUrl = baseProductUrl;
    this.priceUrl = priceUrl;
    this.inventoryUrl = inventoryUrl;
    this.reviewsUrl = reviewsUrl;
  }

  public String getBaseProductUrl() {
    return baseProductUrl;
  }

  public String getPriceUrl() {
    return priceUrl;
  }

  public String getInventoryUrl() {
    return inventoryUrl;
  }

  public String getReviewsUrl() {
    return reviewsUrl;
  }
}
