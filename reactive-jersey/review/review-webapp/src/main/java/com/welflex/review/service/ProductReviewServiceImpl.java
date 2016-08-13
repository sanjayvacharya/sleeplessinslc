package com.welflex.review.service;

import java.util.List;
import java.util.Map;

public class ProductReviewServiceImpl implements ProductReviewService {
  private Map<Long, List<String>> productReviews;
  
  public ProductReviewServiceImpl(Map<Long, List<String>> productReviews) {
    this.productReviews = productReviews;
  }
  
  public List<String> getReviews(Long productId) {
    return productReviews.get(productId);
  }
}
