package com.welflex.example.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.welflex.example.inventory.dto.SellableInventory;
import com.welflex.example.inventory.model.InventoryByLocationSku;
import com.welflex.example.inventory.repository.InventoryByLocationSkuRepository;

@RequestMapping("/inventory")
@RestController
public class SellableInventoryServiceController {

  @Autowired
  private InventoryByLocationSkuRepository inventoryByLocationSku;

  @RequestMapping(value = "/locations/{location}/skus/{sku}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<SellableInventory> getSellableInventory(@PathVariable("location") String location,
      @PathVariable(value = "sku") String sku) {

    InventoryByLocationSku inventory = inventoryByLocationSku
        .findOne(BasicMapId.id().with("location", location).with("sku", sku));

    return (inventory == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<SellableInventory>(new SellableInventory(location, sku, inventory.getCount()),
            HttpStatus.OK);
  }
}
