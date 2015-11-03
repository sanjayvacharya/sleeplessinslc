package com.welflex.notes.client;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableEurekaClient
@PropertySource("classpath:notesclient.properties")
public class NotesClientConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Inject
  private DiscoveryClient discoveryClient;

  @Value("${ocelliRefreshIntervalSeconds}")
  private Long ocelliRefreshIntervalSeconds;

  @Bean(destroyMethod = "close")
  public NotesClient notesClient() {
    return new NotesClientImpl(restTemplate(), discoveryClient,
            ocelliRefreshIntervalSeconds, TimeUnit.SECONDS);
  }
}
