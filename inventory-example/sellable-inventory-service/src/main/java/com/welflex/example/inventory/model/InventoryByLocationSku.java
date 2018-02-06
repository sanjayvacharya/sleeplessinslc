package com.welflex.example.inventory.model;

import java.util.Date;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.datastax.driver.core.DataType.Name;

@org.springframework.data.cassandra.mapping.Table(value = "inventory_by_location_sku")
@Persistent
public class InventoryByLocationSku {

  @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.ASCENDING)
  private String location;

  @PrimaryKeyColumn(name = "sku", ordinal = 1, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.ASCENDING)
  private String sku;

  @Column("count")
  private int count;

  @Column("last_updated")
  @CassandraType(type = Name.TIMESTAMP)
  private Date lastUpdated;
  
  public InventoryByLocationSku() {}
  
  public InventoryByLocationSku(String location, String sku, Integer count, Date lastUpdated) {
    this.location = location;
    this.sku = sku;
    this.count = count;
    this.lastUpdated = lastUpdated;
  }

  public String getLocation() {
    return location;
  }

  public void setLocationId(String location) {
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

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
