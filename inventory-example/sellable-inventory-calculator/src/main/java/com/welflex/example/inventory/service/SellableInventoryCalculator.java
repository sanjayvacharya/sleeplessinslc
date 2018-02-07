package com.welflex.example.inventory.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.welflex.example.inventory.exception.NotFoundException;
import com.welflex.example.inventory.schema.Schemas.Topics;

import io.welflex.examples.inventory.LocationSku;

@Component
public class SellableInventoryCalculator implements SellableInventoryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(SellableInventoryCalculator.class);
  @Autowired
  private StreamsConfig streamsConfig;

  private KafkaStreams sellableInventoryStream;

  static String SELLABLE_INVENTORY_STORE = "sellable_inventory_store";

  @Override
  public Integer getSellableInventory(LocationSku locationSku) throws NotFoundException {
    return sellableInventoryStream
        .store(SELLABLE_INVENTORY_STORE, QueryableStoreTypes.<LocationSku, Integer>keyValueStore()).get(locationSku);
  }

  @PostConstruct
  void start() throws Exception {
    StreamsBuilder kafkaStreamBuilder = new StreamsBuilder();

    KTable<LocationSku, Integer> adjustments = kafkaStreamBuilder
        .stream(Topics.INVENTORY_ADJUSTMENT.name(),
            Consumed.with(Topics.INVENTORY_ADJUSTMENT.keySerde(), Topics.INVENTORY_ADJUSTMENT.valueSerde()))
        .groupByKey(Serialized.<LocationSku, Integer>with(Topics.INVENTORY_ADJUSTMENT.keySerde(),
            Topics.INVENTORY_ADJUSTMENT.valueSerde()))
        .reduce((value1, value2) -> {
          return (value1 + value2);
        });

    KTable<LocationSku, Integer> inventoryReservations = kafkaStreamBuilder
        .stream(Topics.INVENTORY_RESERVATION.name(),
            Consumed.with(Topics.INVENTORY_RESERVATION.keySerde(), Topics.INVENTORY_RESERVATION.valueSerde()))
        .groupByKey(Serialized.<LocationSku, Integer>with(Topics.INVENTORY_RESERVATION.keySerde(),
            Topics.INVENTORY_RESERVATION.valueSerde()))
        .reduce((value1, value2) -> {
          return (value1 + value2);
        });

    KTable<LocationSku, Integer> sellableInventory = adjustments.leftJoin(inventoryReservations,
        (adjustment, reservation) -> {
          LOGGER.info("Adjustment:{} Reservation {}", adjustment, reservation);
          return (adjustment - (reservation == null ? 0 : reservation));
        }, Materialized.<LocationSku, Integer>as(Stores.persistentKeyValueStore(SELLABLE_INVENTORY_STORE))
            .withKeySerde(Topics.SELLABLE_INVENTORY.keySerde()).withValueSerde(Topics.SELLABLE_INVENTORY.valueSerde()));

    sellableInventory.toStream().map((key, value) -> {
      LOGGER.info("Streaming Key: {} Value: {}", key, value);
      return new KeyValue<LocationSku, Integer>(key, value);
    }).to(Topics.SELLABLE_INVENTORY.name(), Produced.<LocationSku, Integer>with(Topics.SELLABLE_INVENTORY.keySerde(),
        Topics.SELLABLE_INVENTORY.valueSerde()));

    sellableInventoryStream = new KafkaStreams(kafkaStreamBuilder.build(), streamsConfig);
    sellableInventoryStream.cleanUp(); // NOTE: Don't do in your app. This only for the scope of this demo
    sellableInventoryStream.start();
  }

  @PreDestroy
  void stop() {
    if (sellableInventoryStream != null) {
      sellableInventoryStream.close();
    }
  }

  @Override
  public HostPort hostPortForKey(LocationSku locationSku) throws NotFoundException {
    StreamsMetadata metadataForKey = sellableInventoryStream.metadataForKey(SELLABLE_INVENTORY_STORE, locationSku,
        Topics.SELLABLE_INVENTORY.keySerde().serializer());

    if (metadataForKey == null) {
      throw new NotFoundException("Not Found");
    }

    return new HostPort(metadataForKey.host(), metadataForKey.port());
  }
}
