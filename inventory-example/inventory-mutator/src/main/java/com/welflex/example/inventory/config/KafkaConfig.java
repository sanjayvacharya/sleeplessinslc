package com.welflex.example.inventory.config;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.welflex.example.inventory.schema.Schemas;

import io.welflex.examples.inventory.LocationSku;

@Configuration
public class KafkaConfig {
  @Value("${spring.kafka.bootstrap.servers}")
  private String bootstrapServers;
  
  private static <K, V> KafkaProducer<K, V> startProducer(String bootstrapServers,
      Serde<K> keySerde, Serde<V> valueSerde) {
    Properties producerConfig = new Properties();
    
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    producerConfig.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(Integer.MAX_VALUE));
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
   
    return new KafkaProducer<>(producerConfig,
        keySerde.serializer(),
        valueSerde.serializer());
  }
  
  @Bean
  public KafkaProducer<LocationSku, Integer> producer() {
	return startProducer(bootstrapServers, Schemas.Topics.LOCATION_SKU_SERDE, 
		new Serdes.IntegerSerde());
  }
  
  @Value("${schema.registry.url}")
  private String schemaRegistryUrl;
  

  @PostConstruct
  public void configureSchemaRegistry() {
	Schemas.configureSerdesWithSchemaRegistryUrl(schemaRegistryUrl);
  }
  
  @PreDestroy
  public void shutdown() {
	producer().close();
  }
}
