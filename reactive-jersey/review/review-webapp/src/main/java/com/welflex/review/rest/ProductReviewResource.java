package com.welflex.review.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.welflex.review.dto.Reviews;
import com.welflex.review.service.ProductReviewService;

@Path("/productReviews/{productId}")
public class ProductReviewResource {
  
  private final ProductReviewService reviewService;
  
  @Inject
  public ProductReviewResource(ProductReviewService reviewService) {
    this.reviewService = reviewService;
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Reviews productReviews(@PathParam("productId") Long productId) {
    List<String> reviews = reviewService.getReviews(productId);
    return new Reviews(productId, reviews);
  }
}
