package com.welflex.review;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.welflex.review.rest.ProductReviewResource;
import com.welflex.review.rest.ReviewExceptionMapper;

public class ReviewApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>>of(ProductReviewResource.class, ReviewExceptionMapper.class);
  }
}
