package com.welflex.example.inventory.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.welflex.example.inventory.dto.InventoryMutationAction;
import com.welflex.example.inventory.schema.Schemas;

import io.welflex.examples.inventory.LocationSku;

@RequestMapping("/inventory")
@RestController
public class InventoryMutationController {
  private static Logger LOGGER = LoggerFactory.getLogger(InventoryMutationController.class);
 
  @Autowired
  private KafkaProducer<LocationSku, Integer> producer;

  @RequestMapping(value = "/adjustment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String updateInventory(@RequestBody InventoryMutationAction inventory) throws Exception {
	LOGGER.info("Received request to update Inventory {}", inventory);
	LocationSku t = new LocationSku(inventory.getLocation(), inventory.getSku());
	Future<RecordMetadata> recordfuture = producer
	    .send(new ProducerRecord<>(Schemas.Topics.INVENTORY_ADJUSTMENT.name(), t, inventory.getCount()));
	
	RecordMetadata metadata = recordfuture.get();
	LOGGER.info("Sent inventory update {} to topic {}", inventory, Schemas.Topics.INVENTORY_ADJUSTMENT.name());
	return String.format("Updated Inventory [%s]", metadata);
  }

  @RequestMapping(value = "/reservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String reserveInventory(@RequestBody InventoryMutationAction inventory) throws InterruptedException, ExecutionException {
	LOGGER.info("Received request to reserve Inventory {}", inventory);
	
	LocationSku t = new LocationSku(inventory.getLocation(), inventory.getSku());
	
	Future<RecordMetadata> recordfuture= producer.send(new ProducerRecord<>(Schemas.Topics.INVENTORY_RESERVATION.name(), t,
	    inventory.getCount()));
	
	RecordMetadata metadata = recordfuture.get();
	LOGGER.info("Sent inventory reservation {} to topic {}", inventory, Schemas.Topics.INVENTORY_RESERVATION.name());
	
	return String.format("Inventory Reservation requested on Partition {%d}", metadata.partition());
  }
}
