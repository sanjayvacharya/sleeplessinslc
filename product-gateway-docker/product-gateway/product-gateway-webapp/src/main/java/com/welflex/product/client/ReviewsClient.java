package com.welflex.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.welflex.review.dto.Reviews;

@FeignClient("reviews")
public interface ReviewsClient {
  @RequestMapping(method = RequestMethod.GET, value = "/productReviews/{productId}", consumes = "application/json")
  Reviews getReviews(@PathVariable("productId") Long productId);
}
