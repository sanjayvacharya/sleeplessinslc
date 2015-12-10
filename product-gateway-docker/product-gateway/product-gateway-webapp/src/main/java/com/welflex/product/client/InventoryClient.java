package com.welflex.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.welflex.inventory.dto.ProductInventory;

@FeignClient("inventory")
public interface InventoryClient {
  @RequestMapping(method = RequestMethod.GET, value = "/productInventory/{id}", consumes = "application/json")
  ProductInventory getInventory(@PathVariable("id") Long id);
}
