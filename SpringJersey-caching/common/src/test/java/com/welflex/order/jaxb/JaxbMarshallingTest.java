package com.welflex.order.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.welflex.order.dto.LineItemDto;
import com.welflex.order.dto.OrderDto;

/**
 * A Unit test to test Marshalling/Unmarshalling of the Transfer objects.
 */
public class JaxbMarshallingTest {

  @Test public void testRequestMarshalling() throws JAXBException {
    OrderDto dto = new OrderDto();
    Long orderId = new Long(123123213);
    dto.setOrderId(orderId);

    ArrayList<LineItemDto> lineItems = new ArrayList<LineItemDto>();
    LineItemDto lineItem = new LineItemDto();

    Long lineItemId = new Long(23231);
    Long itemId = new Long(12123);
    String itemName = "XBOX 360";
    int qty = 2;

    lineItem.setItemId(itemId);
    lineItem.setItemName(itemName);
    lineItem.setLineItemId(lineItemId);
    lineItem.setQuantity(qty);
    lineItem.setProductUri(UriBuilder.fromPath("http://localhost:8080/products").path(lineItemId.toString()).build());

    lineItems.add(lineItem);

    dto.setLineItems(lineItems);

    JAXBContext context = JAXBContext.newInstance(dto.getClass());
    Marshaller marshaller = context.createMarshaller();

    StringWriter writer = new StringWriter();
    marshaller.marshal(dto, writer);

    String outString = writer.toString();
    System.out.println(outString);
    
    assertTrue(outString.contains("</lineItem"));
    assertTrue(outString.contains("</order"));

    Unmarshaller unmarshaller = context.createUnmarshaller();
    StringReader reader = new StringReader(outString);
    dto = (OrderDto) unmarshaller.unmarshal(reader);

    assertEquals(orderId, dto.getOrderId());
    assertEquals(1, dto.getLineItems().size());

    lineItem = dto.getLineItems().get(0);

    assertEquals(lineItemId, lineItem.getLineItemId());
    assertEquals(itemId, lineItem.getItemId());
    assertEquals(itemName, lineItem.getItemName());
    assertEquals(qty, lineItem.getQuantity());
  }
}
