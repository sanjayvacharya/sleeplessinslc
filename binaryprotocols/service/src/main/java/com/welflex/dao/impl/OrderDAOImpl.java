package com.welflex.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.welflex.dao.OrderDAO;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.model.LineItem;
import com.welflex.model.Order;

@Repository
public class OrderDAOImpl implements OrderDAO {
  private Map<Long, Order> ORDER_DB = new HashMap<Long, Order>();

  public boolean delete(Long orderId) {
    return ORDER_DB.remove(orderId) != null;
  }

  public Order getOrder(Long orderId) throws OrderNotFoundException {
    Order order = ORDER_DB.get(orderId);

    if (order == null) {
      throw new OrderNotFoundException("Order:" + orderId + " not found in database:");
    }

    return order;
  }

  public void persist(Order order) {
    Long orderId = order.getOrderId();
    Random random = new Random();
  
    if (orderId == null) {
      long randomId = random.nextLong();

      randomId = (randomId < 0)
          ? (randomId * (-1))
          : randomId;

      orderId = new Long(randomId);
      order.setOrderId(orderId);  
    } 

    for (LineItem lineItem : order.getLineItems()) {
      if (lineItem.getId() == null) {
        long randomId = random.nextLong();
        randomId = (randomId < 0)
            ? (randomId * (-1))
            : randomId;
        lineItem.setId(randomId);
      }
    }
       
    ORDER_DB.put(orderId, order);
  }
}
