package com.welflex.example.inventory.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.welflex.example.inventory.dto.SellableInventory;
import com.welflex.example.inventory.exception.NotFoundException;
import com.welflex.example.inventory.service.SellableInventoryService;
import com.welflex.example.inventory.service.SellableInventoryService.HostPort;

import io.welflex.examples.inventory.LocationSku;

@RestController
public class SellableInventoryServiceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(SellableInventoryServiceController.class);

  @Autowired
  private HostPort hostPort;

  @Autowired
  private SellableInventoryService invCalculator;

  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = "/inventory/locations/{location}/skus/{sku}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public SellableInventory getSellableInventory(@PathVariable("location") String location,
      @PathVariable(value = "sku") String sku, HttpServletRequest request) throws NotFoundException {

    LOGGER.info("[{}] received a request for Location [{}] and SKU [{}]", hostPort, location, sku);

    LocationSku locationSku = new LocationSku(location, sku);
    HostPort hostPortForKey = invCalculator.hostPortForKey(locationSku);

    LOGGER.info("Host Port Computed for Location [{}] and SKU [{}] is [{}]", location, sku, hostPortForKey);

    if (isLocalHost(hostPortForKey)) {
      LOGGER.info("Current Host is the host for the Location [{}] and SKU [{}]", location, sku);

      Integer availableToSell = invCalculator.getSellableInventory(locationSku);
      return new SellableInventory(location, sku, availableToSell == null ? 0 : availableToSell);
    } else {
      LOGGER.info("{} is the host port for the Location {} and SKU {}", hostPortForKey, location, sku);

      String url = String.format("%s://%s:%d/inventory/locations/%s/skus/%s", request.getScheme(),
          hostPortForKey.getHost(), hostPortForKey.getPort(), location, sku);

      LOGGER.info("Invoking {} to obtain sellable inventory", url);

      ResponseEntity<SellableInventory> result = restTemplate.exchange(url, HttpMethod.GET, null,
          SellableInventory.class);

      if (result.getStatusCodeValue() == 200) {
        return result.getBody();
      } else {
        throw new NotFoundException("No Sellable Inventory found for Location " + location + " and SKU " + sku);
      }
    }
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleIOException(NotFoundException ex) {

    return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  private boolean isLocalHost(HostPort hp) {

    if (hp.getPort() != hostPort.getPort()) {
      LOGGER.info("[{}] is not same port as mine [{}]", hp, hostPort);
      return false;
    }

    if (hp.getHost().equals(hostPort.getHost())) {
      LOGGER.info("Host Name [{}] matches my host name [{}]", hp.getHost(), hostPort.getHost());
      return true;
    }

    InetAddress in = null;

    try {
      in = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw new RuntimeException("Error determining host addr", e);
    }

    LOGGER.info("Localhost Name is: {} or {} and Host/Port is {}", in.getHostAddress(), in.getHostName(), hp.getHost());
   
    return (in.getHostName().equals(hp.getHost()) || in.getHostAddress().equals(hp.getHost()));
  }
}
