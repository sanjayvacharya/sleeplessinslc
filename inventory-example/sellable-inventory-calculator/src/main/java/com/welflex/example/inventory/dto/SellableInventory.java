package com.welflex.example.inventory.dto;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class SellableInventory {
  private String location;
  private String sku;
  private int count;
  
  public SellableInventory() {}
  
  public SellableInventory(String location, String sku, int count) {

	this.location = location;
	this.sku = sku;
	this.count = count;
  }

  public String getLocation() {
	return location;
  }

  public void setLocation(String location) {
	this.location = location;
  }

  public String getSku() {
	return sku;
  }

  public void setSku(String sku) {
	this.sku = sku;
  }

  public int getCount() {
	return count;
  }

  public void setCount(int count) {
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
