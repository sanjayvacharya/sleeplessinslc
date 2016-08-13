package com.overstock.inventory.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.overstock.inventory.dto.ProductInventory;
import com.overstock.inventory.service.InventoryService;

@Path("/productInventory/{productId}")
public class InventoryResource {
  private final InventoryService inventoryService;
  
  @Inject
  public InventoryResource(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ProductInventory get(@PathParam("productId") Long productId) {
    com.overstock.inventory.model.ProductInventory inventory = inventoryService.getInventory(productId);

    return new ProductInventory(productId, inventory.getCount());
  }
}
