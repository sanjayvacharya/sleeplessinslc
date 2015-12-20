package com.welflex.xml;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;

public class XmlInjectionTest {
  private static final String ENTITY_INJECT_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  		
      + "<!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]>"
      + "<Person>"
      + " <FirstName>Donald</FirstName>"
      + " <LastName>Duck&xxe;</LastName>"
      + "</Person>";
  
  /**
   * Demonstrates the case where the Person object unmarshalled contains 
   * the contents of the /etc/passwd file as part of the last name 
   * @throws Exception
   */
  @Test
  public void testEntityRead() throws Exception {    
    JAXBContext ctx = JAXBContext.newInstance(Person.class);
    Unmarshaller u = ctx.createUnmarshaller();    
    StringReader reader = new StringReader(ENTITY_INJECT_STRING);
    Person p = (Person) u.unmarshal(reader);

    System.out.println("Person:" + p);
  }
  
  private static final String BAD_PRICE_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
      +  "<item>"
      + " <description>Widget</description>"
       + "<price>500.0</price>"
      + " <quantity>1</quantity>"
      + " <price>1.0</price>" 
      + " <quantity>1</quantity>"
      + "</item>";
  
  private static final String GOOD_PRICE_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
    +  "<item>" 
    +  "<description>Widget</description>"
     + "<price>500.0</price>"
    + " <quantity>1</quantity>"
    + "</item>";

  /**
   * Demonstrates how tag injection works where the price is overriden
   * by an attacker
   */
  @Test
  public void xmlTagInjection() throws Exception {    
    JAXBContext ctx = JAXBContext.newInstance(Item.class);
    Unmarshaller u = ctx.createUnmarshaller();
    // Read a good price string
    StringReader reader = new StringReader(GOOD_PRICE_STRING);
    Item item = (Item) u.unmarshal(reader);
    assertTrue(item.getPrice() == 500.00);
    
    // Read the malevolent price string
    reader = new StringReader(BAD_PRICE_STRING);
    item = (Item) u.unmarshal(reader);
    assertTrue(item.getPrice() == 1.0);
  }
  
  /**
   * Demonstrates how tag injection is prevented by an XSD
   * @throws Exception
   */
  @Test(expected=UnmarshalException.class)
  public void xmlTagInjectionPrevention() throws Exception {
    JAXBContext ctx = JAXBContext.newInstance(Item.class);
    Unmarshaller u = ctx.createUnmarshaller();
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    StreamSource source = new StreamSource(this.getClass().getResourceAsStream("/item.xsd"));
    Schema schema = sf.newSchema(source);
    u.setSchema(schema);
    
    // Read a good price string
    StringReader reader = new StringReader(GOOD_PRICE_STRING);
    Item item = (Item) u.unmarshal(reader);
    assertTrue(item.getPrice() == 500.00);
    
    // Read the injected xml string
    reader = new StringReader(BAD_PRICE_STRING);    
    item = (Item) u.unmarshal(reader);    
  }
  
  private static final String XML_BOMB = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
    + "<!DOCTYPE item["
    + "<!ENTITY item \"item\">"
    + "<!ENTITY item1 \"&item;&item;&item;&item;&item;&item;\">"
    + "<!ENTITY item2 \"&item1;&item1;&item1;&item1;&item1;&item1;&item1;&item1;&item1;\">"
    + "<!ENTITY item3 \"&item2;&item2;&item2;&item2;&item2;&item2;&item2;&item2;&item2;\">"
    + "<!ENTITY item4 \"&item3;&item3;&item3;&item3;&item3;&item3;&item3;&item3;&item3;\">"
    + "<!ENTITY item5 \"&item4;&item4;&item4;&item4;&item4;&item4;&item4;&item4;&item4;\">"
    + "<!ENTITY item6 \"&item5;&item5;&item5;&item5;&item5;&item5;&item5;&item5;&item5;\">"
    + "<!ENTITY item7 \"&item6;&item6;&item6;&item6;&item6;&item6;&item6;&item6;&item6;\">"
    + "<!ENTITY item8 \"&item7;&item7;&item7;&item7;&item7;&item7;&item7;&item7;&item7;\">"
    + "]>"
    + " <item><description>&item8;</description>"
    + "<price>500.0</price>"
    + " <quantity>1</quantity>"
    + "</item>";
  
  /**
   * Demonstrates entity expansion
   * @throws Exception
   */
  @Test(expected=UnmarshalException.class)
  public void testXmlBomb() throws Exception {
    JAXBContext ctx = JAXBContext.newInstance(Item.class);
    Unmarshaller u = ctx.createUnmarshaller();    
    StringReader reader = new StringReader(XML_BOMB);   
    u.unmarshal(reader);    
  }
}
