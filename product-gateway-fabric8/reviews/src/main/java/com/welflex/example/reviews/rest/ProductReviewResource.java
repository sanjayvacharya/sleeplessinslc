package com.welflex.example.reviews.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.welflex.example.reviews.dto.Reviews;

@RestController("/productReviews/{productId}")
public class ProductReviewResource {

  private static Map<Long, List<String>> productReviews = Maps.newHashMap();

  static {
	List<String> reviewComments = Lists.newArrayList();
	reviewComments.add("Very Beautiful");
	reviewComments.add("My husband loved it");
	reviewComments.add("This color is horrible for a work place");
	productReviews.put(9310301L, reviewComments);
  }

  @RequestMapping(value = "/productReviews/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Reviews productReviews(@PathVariable("productId") Long productId) {
	List<String> reviews = productReviews.get(productId);
	return new Reviews(productId, reviews);
  }
}
