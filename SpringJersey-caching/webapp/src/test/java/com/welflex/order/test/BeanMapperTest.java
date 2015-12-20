package com.welflex.order.test;

import junit.framework.Assert;
import net.sf.dozer.util.mapping.DozerBeanMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.welflex.model.LineItem;
import com.welflex.model.Order;
import com.welflex.model.Product;
import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.OrderDto;
import com.welflex.order.dto.ProductDto;

/**
 * Test that ensure the mapping between Order and OrderDTO is accurate.
 */
@RunWith(SpringJUnit4ClassRunner.class)// ApplicationContext will be loaded from
// "/beans.xml"
@ContextConfiguration(locations = { "/applicationContext.xml" }) public class BeanMapperTest {
  @Autowired private DozerBeanMapper dozerBeanMapper;

  @Test public void testMapping() {

    Order order = new Order();
    order.setOrderId(new Long(89812));

    Product product = new Product();

    Long productId = new Long(313);
    String productName = "XBOX";

    product.setId(productId);
    product.setName(productName);
    product.setDescription("Gaming console");

    LineItem lineItem = new LineItem();

    Long lineItemId = new Long(23);
    int quantity = 20;

    lineItem.setId(lineItemId);
    lineItem.setQuantity(quantity);
    lineItem.setProduct(product);

    order.addLineItem(lineItem);

    OrderDto dto = (OrderDto) dozerBeanMapper.map(order, OrderDto.class);

    Assert.assertEquals(new Long(89812), dto.getOrderId());
    Assert.assertEquals(1, dto.getLineItems().size());

    LineItemDto lineItemDTO = dto.getLineItems().get(0);

    Assert.assertEquals(lineItemId, lineItemDTO.getLineItemId());
    Assert.assertEquals(productId, lineItemDTO.getItemId());
    Assert.assertEquals(quantity, lineItemDTO.getQuantity());
    Assert.assertEquals(productName, lineItemDTO.getItemName());

  }

  @Test public void testProductMapping() {
    Product p = new Product();
    Long id = new Long(8918131);
    String name = "XBox 360";
    String description = "XBox Gaming Console. Red Color, Halo Edition";

    p.setId(id);
    p.setName(name);
    p.setDescription(description);

    ProductDto productDTO = (ProductDto) dozerBeanMapper.map(p, ProductDto.class);

    Assert.assertEquals(name, productDTO.getName());
    Assert.assertEquals(id, productDTO.getProductId());
    Assert.assertEquals(description, productDTO.getDescription());

  }
}
