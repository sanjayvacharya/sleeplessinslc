package com.overstock.inventory;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.overstock.inventory.rest.InventoryResource;


public class InventoryApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>>of(InventoryResource.class);
  }
}
