package com.welflex.order.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * An Order class that is the DTO or the to be marshalled object. Note: I have chosen to separate
 * out the data model from the DTO or marshalling object as I prefer to keep them separate.
 * 
 * @author Sanjay Acharya
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "order", propOrder = { "orderId", "lineItems" }) @XmlRootElement(name = "order") public class OrderDto
    implements Serializable {
  private static final long serialVersionUID = 7696713693193497427L;

  @XmlElement(name = "orderIdentifier", required = false) private Long orderId;

  @XmlElement(name = "lineItem", required = false) private ArrayList<LineItemDto> lineItems;

  public OrderDto() {
    lineItems = new ArrayList<LineItemDto>();
  }

  public OrderDto(Long orderId, ArrayList<LineItemDto> lineItems) {
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
  public ArrayList<LineItemDto> getLineItems() {
    return lineItems;
  }

  /**
   * @param items the lineItems to set
   */
  public void setLineItems(ArrayList<LineItemDto> items) {
    this.lineItems = items;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
