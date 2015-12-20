package com.welflex.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item {
  private String description;

  private Double price;

  private int quantity;

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Item [description=" + description + ", price=" + price + ", quantity=" + quantity + "]";
  }
}
