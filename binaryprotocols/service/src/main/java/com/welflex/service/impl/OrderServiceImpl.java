package com.welflex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welflex.dao.OrderDAO;
import com.welflex.dao.ProductDAO;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.exception.OrderValidationException;
import com.welflex.model.LineItem;
import com.welflex.model.Order;
import com.welflex.model.Product;
import com.welflex.service.Auditor;
import com.welflex.service.OrderService;

/**
 * Default Implementation of Order Service. This is a mock implementation.
 * 
 * @author Sanjay Acharya
 */
@Service public class OrderServiceImpl implements OrderService {
  /**
   * Order DAO ref
   */
  @Autowired private OrderDAO orderDao;

  @Autowired private ProductDAO productDAO;

  @Autowired private Auditor auditor;

  public Product getProduct(Long id) {
    return productDAO.getProduct(id);
  }

  private void populateProduct(Order order) {
    for (LineItem lineItem : order.getLineItems()) {
      // Doing this as the product in line item might be a complete
      // product
      // object. This is for demo only not to be taken as any pattern :-)
      lineItem.setProduct(getProduct(lineItem.getProduct().getId()));
    }
  }

  public void persist(Order order) throws OrderValidationException {
    if (order.getLineItems().size() == 0) {
      OrderValidationException ove = new OrderValidationException("Invalid Order");
     
      throw ove;
    }
    
    populateProduct(order);
    System.out.println("Populated Order:" + order);
    orderDao.persist(order);
    auditor.audit("persist", order);
  }

  /**
   * Gets an Order provided the <code>orderId</code>
   * 
   * @param orderId Order Id
   * @return The Order
   * @throws OrderNotFoundException
   */
  public Order getOrder(Long orderId) throws OrderNotFoundException {
    Order order = orderDao.getOrder(orderId);
    auditor.audit("getOrder", order);

    return order;
  }

  /**
   * @param orderId Id of the order to delete.
   */
  public void delete(Long orderId) {
    orderDao.delete(orderId);
    auditor.audit("delete", orderId);
  }

  public void setAuditor(Auditor auditor) {
    this.auditor = auditor;
  }

  public void setOrderDao(OrderDAO orderDao) {
    this.orderDao = orderDao;
  }
}
