package com.welflex.example.reactive;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.overstock.product.dto.Product;

public class IntegrationTest {
  private static final String PRODUCT_GATEWAY_URL = "http://localhost:9091/productGateway";

  private static final Long PRODUCT_ID = 9310301L;

  private Client client;

  @Before
  public void before() {
    ClientConfig config = new ClientConfig();
    config.register(MoxyJsonFeature.class);
    config.register(MoxyXmlFeature.class);
    client = ClientBuilder.newClient(config);
  }

  public static final int MAX_CALLS = 100;

  @Test
  public void serialGetOfProductDemo() {
    Stopwatch watch = new Stopwatch();
    watch.start();

    for (int i = 0; i < MAX_CALLS; i++) {
      client.target(PRODUCT_GATEWAY_URL).path("/products").path(String.valueOf(PRODUCT_ID))
          .request().get(Product.class);

    }
    watch.stop();
    System.out.println("Avg Time for Product Gateway to serially orchestrate:" + watch.elapsedMillis()/MAX_CALLS + "ms");
  }

  @Test
  public void observableGetOfProductDemo() {
    Stopwatch watch = new Stopwatch();
    watch.start();
    for (int i = 0; i < MAX_CALLS; i++) {

      client.target(PRODUCT_GATEWAY_URL).path("/products").path(String.valueOf(PRODUCT_ID))
          .path("/observable").request().get(Product.class);
    }
    watch.stop();
    System.out.println("Avg Time for Product Gateway to use Reactive Jersey to orchestrate:" + watch.elapsedMillis()/MAX_CALLS + "ms");
  }
}
