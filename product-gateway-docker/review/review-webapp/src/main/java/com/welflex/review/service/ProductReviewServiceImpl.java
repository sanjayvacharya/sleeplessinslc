package com.welflex.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
  private Map<Long, List<String>> productReviews;
  
  public ProductReviewServiceImpl() {
    List<String> reviewComments = Lists.newArrayList();
    reviewComments.add("Very Beautiful");
    reviewComments.add("My husband loved it");
    reviewComments.add("This color is horrible for a work place");
    this.productReviews = Maps.newHashMap();
    productReviews.put(9310301L, reviewComments);
  }
  
  public List<String> getReviews(Long productId) {
    return productReviews.get(productId);
  }
}
