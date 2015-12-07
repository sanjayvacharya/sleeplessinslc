package com.welflex.review.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.welflex.review.dto.Reviews;
import com.welflex.review.service.ProductReviewService;

@RestController("/productReviews/{productId}")
public class ProductReviewResource {
  
  private final ProductReviewService reviewService;
  
  @Inject
  public ProductReviewResource(ProductReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @RequestMapping(value = "/productReviews/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Reviews productReviews(@PathVariable("productId") Long productId) {
    List<String> reviews = reviewService.getReviews(productId);
    return new Reviews(productId, reviews);
  }
}
