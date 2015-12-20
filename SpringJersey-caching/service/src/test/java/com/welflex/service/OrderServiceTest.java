package com.welflex.service;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.welflex.dao.OrderDAO;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.model.Order;
import com.welflex.service.impl.OrderServiceImpl;

/**
 * Simple Test to check the working of the Order Service.
 * 
 * @author Sanjay Acharya
 */
public class OrderServiceTest {
  private OrderService orderService;

  private Auditor auditorMock;

  private OrderDAO orderDaoMock;

  @Before public void setup() {
    orderDaoMock = EasyMock.createMock(OrderDAO.class);
    auditorMock = EasyMock.createMock(Auditor.class);
    orderService = new OrderServiceImpl();

    orderService.setOrderDao(orderDaoMock);
    orderService.setAuditor(auditorMock);

  }

  @Test public void testOrderServiceAudit() throws OrderNotFoundException {
    Order order = new Order();

    order.setOrderId(new Long(12313124));

    // Test Persist audit
    orderDaoMock.persist(order);
    auditorMock.audit("persist", order);

    EasyMock.replay(orderDaoMock, auditorMock);

    orderService.persist(order);

    EasyMock.verify(orderDaoMock, auditorMock);

    EasyMock.reset(orderDaoMock, auditorMock);

    // Test Read audit
    Long orderId = new Long(123132);
    order.setOrderId(orderId);

    EasyMock.expect(orderDaoMock.getOrder(orderId)).andReturn(order);
    auditorMock.audit("getOrder", order);

    EasyMock.replay(orderDaoMock, auditorMock);

    orderService.getOrder(orderId);
    EasyMock.verify(orderDaoMock, auditorMock);

    EasyMock.reset(orderDaoMock, auditorMock);

    // Test Delete audit
    EasyMock.expect(orderDaoMock.delete(orderId)).andReturn(true);
    auditorMock.audit("delete", orderId);

    EasyMock.replay(orderDaoMock, auditorMock);

    orderService.delete(orderId);

    EasyMock.verify(orderDaoMock, auditorMock);

  }
}
