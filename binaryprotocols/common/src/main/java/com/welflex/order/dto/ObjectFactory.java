package com.welflex.order.dto;

import javax.xml.bind.annotation.XmlRegistry;

import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.OrderDto;

@XmlRegistry
public class ObjectFactory {
  public ObjectFactory() {}

  public OrderDto createOrderDto() {
    return new OrderDto();
  }

  public LineItemDto createLineItemDto() {
    return new LineItemDto();
  }

  public ProductDto createProductDto() {
    return new ProductDto();
  }

  public ProductListDto createProductListDto() {
    return new ProductListDto();
  }
}
