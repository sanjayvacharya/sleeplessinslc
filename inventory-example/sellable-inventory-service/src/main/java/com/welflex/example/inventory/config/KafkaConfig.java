package com.welflex.example.inventory.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.welflex.example.inventory.schema.Schemas;
import com.welflex.example.inventory.schema.Schemas.Topics;

import avro.shaded.com.google.common.collect.Sets;

@Configuration
public class KafkaConfig {
  @Value("${spring.kafka.bootstrap.servers}")
  private String bootstrapServers;

  @Value("${schema.registry.url}")
  private String schemaRegistryUrl;

  @PostConstruct
  public void configureSchemaRegistry() {
    createTopics(); // Only for Demo
    Schemas.configureSerdesWithSchemaRegistryUrl(schemaRegistryUrl);
  }

  private void createTopics() {
    Properties producerConfig = new Properties();

    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    producerConfig.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(Integer.MAX_VALUE));
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfig.put("auto.create.topics.enable", "true");

    AdminClient adminClient = AdminClient.create(producerConfig);

    adminClient.createTopics(Sets.<NewTopic>newHashSet(new NewTopic(Topics.INVENTORY_ADJUSTMENT.name(), 3, (short) 1),
        new NewTopic(Topics.SELLABLE_INVENTORY.name(), 3, (short) 1),
        new NewTopic(Topics.INVENTORY_RESERVATION.name(), 3, (short) 1)));
  }

  @Bean
  public StreamsConfig streamsConfig() {
    Map<String, Object> props = new HashMap<>();

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "sellable_inventory_service");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

    return new StreamsConfig(props);
  }
}
