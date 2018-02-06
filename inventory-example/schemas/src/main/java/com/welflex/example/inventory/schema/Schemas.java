package com.welflex.example.inventory.schema;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes.IntegerSerde;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.welflex.examples.inventory.LocationSku;

/**
 * This Class is based of: https://github.com/confluentinc/kafka-streams-examples/blob/4.0.0-post/src/main/java/io/confluent/examples/streams/microservices/domain/Schemas.java
 * 
 */
public class Schemas {
  public static class Topic<K, V> {

    private String name;
    private Serde<K> keySerde;
    private Serde<V> valueSerde;

    Topic(String name, Serde<K> keySerde, Serde<V> valueSerde) {
      this.name = name;
      this.keySerde = keySerde;
      this.valueSerde = valueSerde;
      Topics.ALL.put(name, this);
    }

    public Serde<K> keySerde() {
      return keySerde;
    }

    public Serde<V> valueSerde() {
      return valueSerde;
    }

    public String name() {
      return name;
    }

    public String toString() {
      return name;
    }
  }
  
  public static class Topics {
    public static Serde<LocationSku> LOCATION_SKU_SERDE =  new SpecificAvroSerde<>();;
   
    public static Map<String, Topic<?, ?>> ALL = new HashMap<>();
    public static Topic<LocationSku, Integer> INVENTORY_ADJUSTMENT;
    public static Topic<LocationSku, Integer> INVENTORY_RESERVATION;
    public static Topic<LocationSku, Integer> SELLABLE_INVENTORY;
    
    static {
      createTopics();
    }

    private static void createTopics() {
      INVENTORY_ADJUSTMENT = new Topic<>("inventory_adjustment", LOCATION_SKU_SERDE, new IntegerSerde());
      INVENTORY_RESERVATION = new Topic<>("inventory_reservation", LOCATION_SKU_SERDE, new IntegerSerde());
      SELLABLE_INVENTORY = new Topic<>("sellable_inventory", LOCATION_SKU_SERDE, new IntegerSerde());
    }
  }
  
  public static void configureSerdesWithSchemaRegistryUrl(String url) {
    for (Topic<?,?> topic : Topics.ALL.values()) {
      configure(topic.keySerde(), url, true);
      configure(topic.valueSerde(), url, false);
    }
  
    schemaRegistryUrl = url;
  }

  private static void configure(Serde<?> serde, String url, boolean key) {
    if (serde instanceof SpecificAvroSerde) {
      serde.configure(Collections.singletonMap(SCHEMA_REGISTRY_URL_CONFIG, url), key);
    }
  }
  
  public static String schemaRegistryUrl = "";
}
