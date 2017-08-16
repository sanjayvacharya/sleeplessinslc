package com.welflex.example.baseproduct;

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

import com.welflex.example.baseproduct.BaseproductApplication;
import com.welflex.example.baseproduct.dto.BaseProduct;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { BaseproductApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class BaseProductRestApiTest extends Assert {

  TestRestTemplate rest = new TestRestTemplate();

  @Autowired
  EmbeddedWebApplicationContext tomcat;

  int port;

  String baseUri;

  @Before
  public void before() {
	port = tomcat.getEmbeddedServletContainer().getPort();
	baseUri = "http://localhost:" + port + "/baseProduct";
  }

  @Test
  public void getProduct() {
	ResponseEntity<BaseProduct> response = rest.getForEntity(baseUri + "/9310301", BaseProduct.class);
	assertEquals(200, response.getStatusCodeValue());
	BaseProduct baseProduct = response.getBody();
	assertEquals(Long.valueOf(9310301), baseProduct.getProductId());
	assertEquals(4, baseProduct.getOptions().size());
  }
}
