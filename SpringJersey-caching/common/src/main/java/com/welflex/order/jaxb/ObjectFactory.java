package com.welflex.order.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.OrderDto;

@XmlRegistry public class ObjectFactory {
  public ObjectFactory() {}

  /**
   * Create an instance of {@link OrderDto}
   */
  public OrderDto createOrderDto() {
    return new OrderDto();
  }

  public LineItemDto createLineItemDto() {
    return new LineItemDto();
  }
}
