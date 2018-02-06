package com.welflex.example.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * Ensure you have started full suite of applications before running this test
 * 
 */
public class InventoryIntegrationTest {

  private static final String LOCATION = "STL";
  private static final String SKU = "123123";

  private RestTemplate restTemplate = new RestTemplate();

  @Test
  public void sellableInventory() throws InterruptedException {
    int startValue = getFromSellableInventoryService();

    // Adjust the inventory
    adjustInventory(100);

    Thread.sleep(1000); // TODO be more deterministic, maybe consume from a topic

    int expectedSellableInventory = startValue + 100;

    assertEquals("Kafka Stream Store must have the update or one of its peers should have it", expectedSellableInventory, getSellableInventoryFromKafkaStreamStore(9094));
    assertEquals("Kafka Stream Store must have the update or one of its peers should have it", expectedSellableInventory, getSellableInventoryFromKafkaStreamStore(9095));
    assertEquals("Sellable Inventory Service must have the inventory update", expectedSellableInventory, getFromSellableInventoryService());
  
    // Reserve inventory
    reserveInventory(10);

    Thread.sleep(1000);// TODO change to more deterministic, maybe under test
                       // send a msg

    expectedSellableInventory = expectedSellableInventory - 10;

    assertEquals("Kafka Stream Store must have the update or one of its peers should have it", expectedSellableInventory, getSellableInventoryFromKafkaStreamStore(9094));
    assertEquals("Kafka Stream Store must have the update or one of its peers should have it", expectedSellableInventory, getSellableInventoryFromKafkaStreamStore(9095));
    assertEquals("Sellable Inventory Service must have the inventory update", expectedSellableInventory, getFromSellableInventoryService());
  }

  /**
   * Invokes the Inventory Mutator to adjust the inventory by the
   * {@code quantity} provided
   * 
   * @param quantity Quantity by which to update the inventory
   */
  private void adjustInventory(int quantity) {
    InventoryDto invadjustment = new InventoryDto(LOCATION, SKU, quantity);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:9091/inventory/adjustment",
        HttpMethod.POST, new HttpEntity<InventoryDto>(invadjustment, headers), String.class);

    assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
  }
  
  /**
   * Sends a request to reserve Inventory of {@code quantity}
   * 
   * @param quantity Quantity by which to reserve the inventory
   */
  private void reserveInventory(int quantity) {
    InventoryDto invadjustment = new InventoryDto(LOCATION, SKU, quantity);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:9091/inventory/reservation",
        HttpMethod.POST, new HttpEntity<InventoryDto>(invadjustment, headers), String.class);

    assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
  }

  /**
   * Gets the Sellable inventory from the Sellable Inventory service
   * @return Sellable inventory
   */
  private int getFromSellableInventoryService() {
    Map<String, String> pathVars = Maps.newHashMap();
    pathVars.put("location", LOCATION);
    pathVars.put("sku", SKU);
    try {
      InventoryDto inventory = restTemplate.getForObject(
          "http://localhost:9093/inventory/locations/{location}/skus/{sku}", InventoryDto.class, pathVars);

      return inventory.getCount();
    } catch (RestClientException e) {
      return 0;
    }
  }

  /**
   * Gets the Sellable inventory from the Kafka Stream store
   * @return Sellable inventory
   */
  private int getSellableInventoryFromKafkaStreamStore(int port) {
    Map<String, String> pathVars = Maps.newHashMap();
    pathVars.put("location", LOCATION);
    pathVars.put("sku", SKU);

    InventoryDto inventory = restTemplate.getForObject(
        "http://localhost:" + port + "/inventory/locations/{location}/skus/{sku}", InventoryDto.class, pathVars);

    return inventory.getCount();
  }

  static class InventoryDto {
    private String location;
    private String sku;
    private int count;

    public InventoryDto() {
    }

    public InventoryDto(String location, String sku, int count) {
      this.location = location;
      this.sku = sku;
      this.count = count;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public String getSku() {
      return sku;
    }

    public void setSku(String sku) {
      this.sku = sku;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }
  }
}
