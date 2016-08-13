package com.welflex.price;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.welflex.price.rest.PricingResource;

public class PricingApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>>of(PricingResource.class);
  }
}
