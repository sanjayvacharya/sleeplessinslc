package com.welflex.order;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.welflex.client.OrderClient;
import com.welflex.client.OrderClientImpl;
import com.welflex.client.ProductClient;
import com.welflex.client.ProductClientImpl;
import com.welflex.exception.OrderNotFoundException;
import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.OrderDto;
import com.welflex.order.dto.ProductDto;
import com.welflex.order.dto.ProductListDto;

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

  private static OrderClient ORDER_CLIENT = null;

  private static ProductClient PRODUCT_CLIENT = null;

  static {
    try {
      ORDER_CLIENT = new OrderClientImpl(INTEGRATION_TEST_URI);
      PRODUCT_CLIENT = new ProductClientImpl(INTEGRATION_TEST_URI);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private OrderDto createOrderDTO(List<ProductDto> products) {
    Iterator<ProductDto> i = products.iterator();
    ArrayList<LineItemDto> lineItems = new ArrayList<LineItemDto>();

    LineItemDto lineItem = new LineItemDto();

    ProductDto product = i.next();
    i.remove();
    System.out.println("Product Id:" + product.getProductId());
    lineItem.setItemId(product.getProductId());
    lineItem.setItemName(product.getName());
    lineItem.setQuantity(2);

    lineItems.add(lineItem);

    product = i.next();
    i.remove();
    lineItem = new LineItemDto();
    System.out.println("Product Id:" + product.getProductId());
    lineItem.setItemId(product.getProductId());
    lineItem.setItemName(product.getName());
    lineItem.setQuantity(3);

    lineItems.add(lineItem);

    OrderDto order = new OrderDto();

    order.setLineItems(lineItems);

    return order;
  }

  @Test
  public void testGetProducts() throws Exception {
    ProductListDto products = PRODUCT_CLIENT.getProducts();
    Assert.assertFalse(products.getProducts().size() == 0);

    // Get the product again
    products = PRODUCT_CLIENT.getProducts();
    System.out.println("Sleeping for 5 seconds so that product list expires...");
    Thread.sleep(5000);
    products = PRODUCT_CLIENT.getProducts();
  }

  @Test public void testLifeCycle() throws Exception {
    List<ProductDto> products = PRODUCT_CLIENT.getProducts().getProducts();
    List<ProductDto> productList = new ArrayList<ProductDto>(products);
    OrderDto order = createOrderDTO(productList);

    // Create The Order
    System.out.println("Storing the order...");
    order = ORDER_CLIENT.createOrder(order);

    Assert.assertNotNull(order.getOrderId());
    Assert.assertEquals(2, order.getLineItems().size());

    for (LineItemDto lineItem : order.getLineItems()) {
      Assert.assertNotNull(lineItem.getLineItemId());
      Assert.assertNotNull(lineItem.getItemName());
      Assert.assertFalse(lineItem.getQuantity() == 0);
    }

    // Read the order in
    System.out.println("Reading in order after creation...");
    System.out.println("Retrieving the order...");

    order = ORDER_CLIENT.getOrder(order.getOrderId());
    Assert.assertNotNull(order);
    Assert.assertNotNull(order.getOrderId());

    // Obtaining order again, should see it being pulled from cache
    order = ORDER_CLIENT.getOrder(order.getOrderId());
    Assert.assertNotNull(order);

    // Update the Order
    LineItemDto lineItem = new LineItemDto();

    Iterator<ProductDto> i = productList.iterator();
    ProductDto product = i.next();

    lineItem.setItemId(product.getProductId());
    lineItem.setItemName(product.getName());
    lineItem.setQuantity(2);

    order.getLineItems().add(lineItem);

    System.out.println("Updating the order...");
    ORDER_CLIENT.updateOrder(order, order.getOrderId());

    // Get the Order
    System.out.println("Retrieving the order..should not obtained cached copy...");
    order = ORDER_CLIENT.getOrder(order.getOrderId());
    Assert.assertNotNull(order);
    Assert.assertNotNull(order.getOrderId());
    Assert.assertEquals(3, order.getLineItems().size());

    // Delete the Order'
    Long orderId = order.getOrderId();

    System.out.println("Deleting the order..");
    ORDER_CLIENT.deleteOrder(orderId);
    System.out.println("Client Deleted the order...");

    // Get the Order again, should get a not found exception
    try {
      System.out.println("Obtaining order after deletion..");
      order = ORDER_CLIENT.getOrder(orderId);
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
