package com.welflex.example.inventory.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.rocksdb.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.welflex.example.inventory.schema.Schemas;
import com.welflex.example.inventory.schema.Schemas.Topics;
import com.welflex.example.inventory.service.SellableInventoryService;

import avro.shaded.com.google.common.collect.Sets;

@Configuration
public class SellableInventoryCalculatorConfig {

  @Value("${schema.registry.url}")
  private String schemaRegistryUrl;

  @Value("${spring.kafka.bootstrap.servers}")
  private String bootstrapServers;

  @Value("${server.host}")
  private String serverHost;

  @Value("${server.port:9094}")
  private int serverPort;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public SellableInventoryService.HostPort getAppHostPort() throws UnknownHostException {
    if (serverHost == null || "".equals(serverHost)) {
      return new SellableInventoryService.HostPort(InetAddress.getLocalHost().getHostName(), serverPort);
    } else {
      return new SellableInventoryService.HostPort(serverHost, serverPort);
    }
  }
  
  //https://groups.google.com/forum/#!topic/confluent-platform/nmfrnAKCM3c
  private void createTopics() {
    Properties producerConfig = new Properties();

    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    producerConfig.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(Integer.MAX_VALUE));
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");

    AdminClient adminClient = AdminClient.create(producerConfig);

    adminClient.createTopics(Sets.<NewTopic>newHashSet(new NewTopic(Topics.INVENTORY_ADJUSTMENT.name(), 3, (short) 1),
        new NewTopic(Topics.SELLABLE_INVENTORY.name(), 3, (short) 1),
        new NewTopic(Topics.INVENTORY_RESERVATION.name(), 3, (short) 1)));
  }
  
  @PostConstruct
  public void postConstructOperations() {
    Schemas.configureSerdesWithSchemaRegistryUrl(schemaRegistryUrl);
    createTopics(); //NOTE: This is only for Demo purpose, do not do it in code and create topic external
  }
  
  @Bean
  public StreamsConfig streamConfig() throws UnknownHostException {
   
    Map<String, Object> props = new HashMap<>();

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "sellableInventoryCalculator");
    props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, getAppHostPort().getHost() + ":" + serverPort);
    props.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, CustomRocksDBConfig.class);
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);
    
    return new StreamsConfig(props);
  }

  public static class CustomRocksDBConfig implements RocksDBConfigSetter {

    @Override
    public void setConfig(final String storeName, final Options options, final Map<String, Object> configs) {
      // Workaround: We must ensure that the parallelism is set to >= 2. There
      // seems to be a known
      // issue with RocksDB where explicitly setting the parallelism to 1 causes
      // issues (even though
      // 1 seems to be RocksDB's default for this configuration).
      int compactionParallelism = Math.max(Runtime.getRuntime().availableProcessors(), 2);
      // Set number of compaction threads (but not flush threads).
      options.setIncreaseParallelism(compactionParallelism);
    }
  }
}
