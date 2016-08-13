package com.welflex.review;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.welflex.review.service.ProductReviewService;
import com.welflex.review.service.ProductReviewServiceImpl;

@Configuration
public class ReviewJavaConfig { 
  @Bean
  public ProductReviewService productReviewService() {
    Map<Long, List<String>> reviews = Maps.newHashMap();
    
    List<String> reviewComments = Lists.newArrayList();
    reviewComments.add("Very Beautiful");
    reviewComments.add("My husband loved it");
    reviewComments.add("This color is horrible for a work place");
    
    reviews.put(9310301L, reviewComments);
    return new ProductReviewServiceImpl(reviews);
  }
}
