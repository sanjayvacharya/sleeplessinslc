package com.welflex.order.dto;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Defines a Line Item of an Order
 * 
 * @author sacharya
 */
@XmlType(name = "lineItem", propOrder = { "lineItemId", "itemId", "itemName", "quantity", "productUri" }) 
@XmlRootElement(name = "lineItem") 
public class LineItemDto implements Serializable {
  private static final long serialVersionUID = -3943002191432271383L;

  private Long lineItemId;

  private Long itemId;

  private String itemName;

  private int quantity;
  
  private URI productUri;
  

  public LineItemDto() {}

  /**
   * @return the lineItemId
   */
  public Long getLineItemId() {
    return lineItemId;
  }

  /**
   * @param lineItemId the lineItemId to set
   */
  public void setLineItemId(Long lineItemId) {
    this.lineItemId = lineItemId;
  }

  /**
   * @return the itemName
   */
  public String getItemName() {
    return itemName;
  }

  /**
   * @param itemName the itemName to set
   */
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  /**
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * @param quantity the quantity to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }
 

  public URI getProductUri() {
    return productUri;
  }

  public void setProductUri(URI productUri) {
    this.productUri = productUri;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
