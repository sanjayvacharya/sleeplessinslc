package com.welflex.example.inventory.dto;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class InventoryMutationAction {
  private String location;
  private String sku;
  private Integer count;

  public String getSku() {
	return sku;
  }

  public Integer getCount() {
	return count;
  }

  public String getLocation() {
	return location;
  }

  public void setLocationId(String location) {
	this.location = location;
  }

  public void setSku(String sku) {
	this.sku = sku;
  }

  public void setCount(Integer count) {
	this.count = count;
  }
  
  @Override
  public String toString() {
	return Pojomatic.toString(this);
  }
  
  @Override
  public int hashCode() {
	return Pojomatic.hashCode(this);
  }
  
  @Override
  public boolean equals(Object other) {
	return Pojomatic.equals(this, other);
  }
}
