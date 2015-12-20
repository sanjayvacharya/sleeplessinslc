package com.welflex.order;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.welflex.clients.OrdersClient;
import com.welflex.clients.ProductsClient;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.orders.proto.dto.OrderProtos;
import com.welflex.orders.proto.dto.OrderProtos.LineItem;
import com.welflex.products.proto.dto.ProductProtos;
import com.welflex.products.proto.dto.ProductProtos.Product;

/**
 * A Simple Integration Test. Note that this test makes a lot of assumptions.
 */
public class IntegrationTest {
  /**
   * Statically defined URI for the integration test. Can be obtained via resource if need be. Note
   * that by default the integration test is configured to be available at the context root of
   * IntegrationTest
   */
  private static final String INTEGRATION_TEST_URI = "http://localhost:8080/IntegrationTest";

  private static OrdersClient ORDER_CLIENT = null;

  private static ProductsClient PRODUCT_CLIENT = null;

  static {
    try {
      ORDER_CLIENT = new OrdersClient(INTEGRATION_TEST_URI);
      PRODUCT_CLIENT = new ProductsClient(INTEGRATION_TEST_URI);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private OrderProtos.Order createOrderDto(List<ProductProtos.Product> products) {
    OrderProtos.Order.Builder builder = OrderProtos.Order.newBuilder();
    
    Iterator<ProductProtos.Product> i = products.iterator();
    Product product = i.next();
    
    LineItem lineItem = LineItem.newBuilder().setItemId(product.getId())
      .setItemName(product.getName()).setQuantity(2).build();
    builder.addLineItems(lineItem);
    
    product = i.next();    
    lineItem = LineItem.newBuilder().setItemId(product.getId())
      .setItemName(product.getName()).setQuantity(2).build();
    builder.addLineItems(lineItem);
    
    return builder.build();
  }
  
  @Test
  public void testGetProducts() throws Exception {
    ProductProtos.ProductList products = PRODUCT_CLIENT.getProductList();
    Assert.assertFalse(products.getProductDtoCount() == 0);
  }

  @Test public void testLifeCycle() throws Exception {
    ProductProtos.ProductList products = PRODUCT_CLIENT.getProductList();
    OrderProtos.Order order = createOrderDto(products.getProductDtoList());
    
    // Create The Order
    System.out.println("Storing the order...");
    order = ORDER_CLIENT.create(order);
    Assert.assertNotNull(order.getId());
    Assert.assertEquals(2, order.getLineItemsList().size());
    
    // Read the order in
    System.out.println("Reading in order after creation...");
    
    order = ORDER_CLIENT.get(order.getId());
    Assert.assertNotNull(order);
    
    // Obtaining order again, should see it being pulled from cache
    order = ORDER_CLIENT.get(order.getId());
    Assert.assertNotNull(order);

    // Update the Order
    Product product = products.getProductDtoList().get(products.getProductDtoCount() - 1);
    LineItem l = LineItem.newBuilder().setItemId(product.getId())
      .setItemName(product.getName()).setQuantity(2).build();
    order = OrderProtos.Order.newBuilder(order).addLineItems(l).build();
    
    
    System.out.println("Updating the order...");
    ORDER_CLIENT.update(order);

    // Get the Order
    System.out.println("Retrieving the order after update...");
    order = ORDER_CLIENT.get(order.getId());
    Assert.assertNotNull(order);
    Assert.assertNotNull(order.getId());
    Assert.assertEquals(3, order.getLineItemsCount());

    // Delete the Order'
    Long orderId = order.getId();

    System.out.println("Deleting the order..");
    ORDER_CLIENT.delete(orderId);
    System.out.println("Client Deleted the order...");

    // Get the Order again, should get a not found exception
    try {
      System.out.println("Obtaining order after deletion..");
      order = ORDER_CLIENT.get(orderId);
      if (order == null) {
        throw new OrderNotFoundException("Order was null as expected");
      }
      fail("Order must  have been found");
    }
    catch (OrderNotFoundException e) {
      // Expected
    }
  }
}
