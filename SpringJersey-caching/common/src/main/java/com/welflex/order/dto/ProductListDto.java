package com.welflex.order.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "productList")
@XmlAccessorType(XmlAccessType.FIELD) 
@XmlType(name = "products", propOrder = {"productDtos"})
public class ProductListDto implements Iterable<ProductDto> {
  private List<ProductDto> productDtos;
  
  public ProductListDto() {}
  
  public ProductListDto(Set<ProductDto> productDtos) {
    this.productDtos = new ArrayList<ProductDto>(productDtos);
  }

  public List<ProductDto> getProducts() {
    return productDtos;
  }

  public Iterator<ProductDto> iterator() {
    return productDtos.iterator();
  }

  public void setProductDtos(List<ProductDto> ProductDtos) {
    this.productDtos = ProductDtos;
  }

}
