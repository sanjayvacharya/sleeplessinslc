package com.welflex.order.rest.util;

import java.util.Set;

import com.welflex.model.Order;
import com.welflex.model.Product;
import com.welflex.orders.proto.dto.OrderProtos;
import com.welflex.orders.proto.dto.OrderProtos.LineItem;
import com.welflex.products.proto.dto.ProductProtos;
import com.welflex.products.proto.dto.ProductProtos.ProductList;
import com.welflex.products.proto.dto.ProductProtos.ProductList.Builder;

public class Utils {
  private Utils() {}

  public static OrderProtos.Order build(Order order) {
    OrderProtos.Order.Builder orderBuilder = OrderProtos.Order.newBuilder();
    orderBuilder.setId(order.getOrderId());
    
    for (com.welflex.model.LineItem l : order.getLineItems()) {
      OrderProtos.LineItem pl = OrderProtos.LineItem.newBuilder().setId(l.getId()).setItemId(
        l.getProduct().getId()).setItemName(l.getProduct().getName()).setQuantity(l.getQuantity())
        .build();
    
      orderBuilder.addLineItems(pl);
    }
    
    return orderBuilder.build();
  }

  public static Order build(OrderProtos.Order orderDto) {
    Order order = new Order();
    order.setOrderId(orderDto.getId()  == 0 ? null : orderDto.getId());

    for (LineItem item : orderDto.getLineItemsList()) {
      com.welflex.model.LineItem l = new com.welflex.model.LineItem();
      l.setId(item.getId() == 0 ? null : item.getId());
      l.setQuantity(item.getQuantity());
      Product p = new Product();
      p.setId(item.getItemId());
      p.setName(item.getItemName());
      l.setProduct(p);
      order.addLineItem(l);
    }
    return order;
  }
  
  public static ProductList build(Set<Product> products) {
    Builder b = ProductProtos.ProductList.newBuilder();

    for (Product product : products) {
      b.addProductDto(ProductProtos.Product.newBuilder()
        .setId((int) product.getId().longValue()) 
        .setName(product.getName()).setDescription(product.getDescription())
          .build());
    }

    return b.build();
  }

}
