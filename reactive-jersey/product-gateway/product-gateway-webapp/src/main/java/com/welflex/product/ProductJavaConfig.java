package com.welflex.product;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductJavaConfig {
  @Value("#{systemProperties['baseProductUrl']}")
  private String baseProductUrl;
  
  @Value("#{systemProperties['reviewUrl']}")
  private String reviewsUrl;
  
  @Value("#{systemProperties['priceUrl']}")
  private String priceUrl;
  
  @Value("#{systemProperties['inventoryUrl']}")
  private String inventoryUrl;
    
  @Bean
  public Client jerseyClient() {
    ClientConfig config = new ClientConfig();
    
    config.register(MoxyJsonFeature.class);
    config.register(MoxyXmlFeature.class);
    
    return ClientBuilder.newClient(config);
  }
  
  @Bean
  public ServiceUrls serviceUrls() {
    return new ServiceUrls(baseProductUrl, priceUrl, inventoryUrl, reviewsUrl);
  }
}
