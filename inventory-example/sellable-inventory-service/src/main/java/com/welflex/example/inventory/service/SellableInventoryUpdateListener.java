package com.welflex.example.inventory.service;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.welflex.example.inventory.model.InventoryByLocationSku;
import com.welflex.example.inventory.repository.InventoryByLocationSkuRepository;
import com.welflex.example.inventory.schema.Schemas.Topics;

import io.welflex.examples.inventory.LocationSku;

@Component
public class SellableInventoryUpdateListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(SellableInventoryUpdateListener.class);

  @Autowired
  private InventoryByLocationSkuRepository sellableInventoryRepository;

  @Autowired
  private StreamsConfig sellableInventoryStreamConfig;

  private KafkaStreams sellableInventoryEventStream;

  @PostConstruct
  public void start() throws Exception {
    LOGGER.info("Starting Sellable Inventory Calculator...");
    StreamsBuilder sellableInventoryStreamBuilder = new StreamsBuilder();

    KStream<LocationSku, Integer> stream = sellableInventoryStreamBuilder.stream(Topics.SELLABLE_INVENTORY.name(),
        Consumed.with(Topics.SELLABLE_INVENTORY.keySerde(), Topics.SELLABLE_INVENTORY.valueSerde()));

    stream.foreach((key, value) -> {
      LOGGER.info("Got an Event Key {} Value {}", key, value);
      // Persist to DB
      sellableInventoryRepository.save(new InventoryByLocationSku(
        key.getLocation(), key.getSku(),
        value, new Date()));
    });

    sellableInventoryEventStream = new KafkaStreams(sellableInventoryStreamBuilder.build(),
        sellableInventoryStreamConfig);
    sellableInventoryEventStream.cleanUp();// NOTE: Don't do this in your app. This only for the scope of this demo
    sellableInventoryEventStream.start();
  }
  
  @PreDestroy
  public void stop() {
    if (sellableInventoryEventStream != null) {
      sellableInventoryEventStream.close();
    }
  }
}
