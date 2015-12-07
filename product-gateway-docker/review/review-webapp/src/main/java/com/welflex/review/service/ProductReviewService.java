package com.welflex.review.service;

import java.util.List;

public interface ProductReviewService {
  public List<String> getReviews(Long productId);
}
