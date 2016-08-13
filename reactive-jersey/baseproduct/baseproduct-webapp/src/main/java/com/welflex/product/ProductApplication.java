package com.welflex.product;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.welflex.product.rest.BaseProductResource;

public class ProductApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>>of(BaseProductResource.class);
  }
}
