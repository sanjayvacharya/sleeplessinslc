package com.welflex.example.inventory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.welflex.example.inventory.InventoryApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { InventoryApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class InventoryRestApiTest extends Assert {

  TestRestTemplate rest = new TestRestTemplate();

  @Autowired
  EmbeddedWebApplicationContext tomcat;

  int port;

  String baseUri;

  @Before
  public void before() {
	port = tomcat.getEmbeddedServletContainer().getPort();
	baseUri = "http://localhost:" + port + "/productInventory";
  }

  @Test
  public void getInventory() {
	ResponseEntity<String> response = rest.getForEntity(baseUri + "/1", String.class);
	assertEquals(200, response.getStatusCodeValue());
	assertEquals(new Integer(20), Integer.valueOf(response.getBody()));
  }
}
