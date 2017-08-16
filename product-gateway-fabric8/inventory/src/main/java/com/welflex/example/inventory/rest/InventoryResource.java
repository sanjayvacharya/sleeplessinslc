package com.welflex.example.inventory.rest;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

@RestController
public class InventoryResource {
  private static Map<Long, Integer> productInventory = Maps.newHashMap();

  static {
	productInventory.put(1L, 20);
	productInventory.put(2L, 32);
	productInventory.put(3L, 41);
	productInventory.put(4L, 0);
	productInventory.put(9310301L, 20 + 32 + 41);
  }

  @RequestMapping(value = "/productInventory/{productId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String get(@PathVariable("productId") Long productId) {
	return String.valueOf(productInventory.get(productId));
  }
}
