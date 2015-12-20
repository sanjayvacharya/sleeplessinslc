package com.welflex.service;

import java.util.List;

import com.welflex.model.Order;

public interface Auditor {
  /**
   * Audits an Order
   * 
   * @param order Order
   */
  public void audit(String operation, Order order);

  /**
   * Audits an order id
   * 
   * @param orderId Id of an order
   */
  public void audit(String operation, Long orderId);

  /**
   * @return a List of audited messages.
   */
  public List<String> getAuditedMessages();
}
