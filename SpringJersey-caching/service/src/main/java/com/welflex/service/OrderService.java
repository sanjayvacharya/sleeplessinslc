package com.welflex.service;

import com.welflex.dao.OrderDAO;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.model.Order;
import com.welflex.model.Product;

public interface OrderService {
  /**
   * Creates an Order
   * 
   * @param order Order
   * @return Order Identifier
   */
  public void persist(Order order);

  /**
   * Gets an Order provided the <code>orderId</code>
   * 
   * @param orderId Order Id
   * @return The Order
   */
  public Order getOrder(Long orderId) throws OrderNotFoundException;

  /**
   * Gets a Product provided the <code>id</code>
   * 
   * @param id A Product id
   * @return A Product
   */
  public Product getProduct(Long id);

  /**
   * @param orderId Id of the order to delete.
   */
  public void delete(Long orderId);

  /**
   * An Auditor.
   * 
   * @param auditor An Auditor.
   */
  public void setAuditor(Auditor auditor);

  /**
   * @param orderDao Order DAO
   */
  public void setOrderDao(OrderDAO orderDao);
}
