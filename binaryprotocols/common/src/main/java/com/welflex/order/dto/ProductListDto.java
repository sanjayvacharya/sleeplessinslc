package com.welflex.order.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "productList") @XmlRootElement(name = "productList")
public class ProductListDto implements Iterable<ProductDto> {
  @XmlElement(name = "products", required = false)
  private List<ProductDto> productDTOs = new ArrayList<ProductDto>();
  
  public List<ProductDto> getProductDTOs() {
    return productDTOs;
  }

  public void setProductDTOs(List<ProductDto> productDTOs) {
    this.productDTOs = productDTOs;
  }    
  
  public void addAll(Set<ProductDto> productDTOs) {
    this.productDTOs.addAll(productDTOs);
  }

  public Iterator<ProductDto> iterator() {
    return productDTOs.iterator();
  }
}
