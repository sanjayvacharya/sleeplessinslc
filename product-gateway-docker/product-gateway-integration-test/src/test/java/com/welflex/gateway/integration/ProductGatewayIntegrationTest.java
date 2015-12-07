package com.welflex.gateway.integration;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.overstock.product.dto.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ProductGatewayIntegrationTest.IntegrationTestConfig.class })
public class ProductGatewayIntegrationTest {
  private static final Logger LOGGER = Logger.getLogger(ProductGatewayIntegrationTest.class);
  
  /**
   * A Feign Client to obtain a Product
   */
  @FeignClient("product-gateway")
  public static interface ProductClient {
    @RequestMapping(method = RequestMethod.GET, value = "/products/{productId}", consumes = "application/json")
    Product getProduct(@PathVariable("productId") Long productId);
  }

  @EnableFeignClients
  @EnableDiscoveryClient
  @EnableAutoConfiguration
  @ComponentScan
  @Configuration
  public static class IntegrationTestConfig {}

  // Ribbon Load Balancer Client used for testing to ensure an instance is available before invoking call
  @Autowired
  LoadBalancerClient loadBalancerClient;

  @Inject
  private ProductClient productClient;

  static final Long PRODUCT_ID = 9310301L;

  @Test(timeout = 30000)
  public void getProduct() throws InterruptedException {
    waitForGatewayDiscovery();
    Product product = productClient.getProduct(PRODUCT_ID);
    assertNotNull(product);
  }

  /**
   * Waits for the product gateway service to register with Eureka
   * and be available on the client.
   */
  private void waitForGatewayDiscovery() {
    while (!Thread.currentThread().isInterrupted()) {
      LOGGER.debug("Checking to see if an instance of product-gateway is available..");
      ServiceInstance choose = loadBalancerClient.choose("product-gateway");
      if (choose != null) {
        LOGGER.debug("An instance of product-gateway was found. Test can proceed.");
        break;
      }
      try {
        LOGGER.debug("Sleeping for a second waiting for service discovery to catch up");
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
