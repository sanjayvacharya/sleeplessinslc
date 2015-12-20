package com.welflex.order.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Product Data transfer object.
 * 
 * @author Sanjay Acharya
 */
@XmlType(name = "product") 
@XmlRootElement(name = "product") 
public class ProductDto implements Serializable {

  private static final long serialVersionUID = 1357833548997122849L;

  private Long productId;

  private String name;

  private String description;

  public ProductDto() {}

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
