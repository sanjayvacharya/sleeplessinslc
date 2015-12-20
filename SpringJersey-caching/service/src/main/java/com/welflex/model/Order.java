package com.welflex.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * An Order class that represents the underlying data model.
 *
 * @author Sanjay Acharya
 */
public class Order implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long orderId;

  private ArrayList<LineItem> lineItems;

  private Date lastUpdateDate;

  public Order() {
    lineItems = new ArrayList<LineItem>();
  }

  public Order(Long orderId, ArrayList<LineItem> lineItems) {
    this.orderId = orderId;
    this.lineItems = lineItems;
  }

  /**
   * @return the orderId
   */
  public Long getOrderId() {
    return orderId;
  }

  /**
   * @param orderId the orderId to set
   */
  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  /**
   * @return the lineItems
   */
  public ArrayList<LineItem> getLineItems() {
    return lineItems;
  }

  /**
   * @param items the lineItems to set
   */
  public void setLineItems(ArrayList<LineItem> items) {
    this.lineItems = items;
  }

  /**
   * @param lineItem A Line Item to add.
   */
  public void addLineItem(LineItem lineItem) {
    lineItems.add(lineItem);
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
