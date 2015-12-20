package com.welflex.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.welflex.order.dto.ProductListDto;

/**
 * An Implementation of the {@link ProductClient} that
 * delegates all its processing to a RestEasy Proxy client.
 *
 * @author Sanjay Acharya
 */
public class ProductClientImpl implements ProductClient {
  private final WebResource resource;
  private static final Logger LOG = Logger.getLogger(ProductClientImpl.class);

  /**
   * @param uri Server Uri
   */
  public ProductClientImpl(String uri) {
    ClientConfig cc = new DefaultClientConfig();
    Client delegate = Client.create(cc);
    resource = delegate.resource(uri).path("products");
  }

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

  public ProductListDto getProducts() {
    String path = resource.getURI().getPath();

    CacheEntry entry = CacheManager.get(path);

    ProductListDto productList = null;

    if (entry != null) {
      LOG.debug("Product Entry in cache is not null...checking expiration date..");
      Date cacheTillDate = entry.getCacheTillDate();
      Date now = new Date();

      if (now.before(cacheTillDate)) {
        LOG.debug("Product List is not stale..using cached value");
        productList =  (ProductListDto) entry.getObject();
      } else {
        LOG.debug("Product List is stale..will request server for new Product List..");
      }
    }

    if (productList == null) {
      LOG.debug("Fetching Product List from Service...");
      ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

      if (response.getResponseStatus().equals(Status.OK)) {
        productList = response.getEntity(ProductListDto.class);
        String cacheDate = response.getMetadata().getFirst("Expires");

        if (cacheDate != null) {
          Date ccDate;
          try {
            ccDate = DATE_FORMAT.parse(cacheDate);
            entry = new CacheEntry(productList, null, ccDate);
            CacheManager.cache(path, entry);
          }
          catch (ParseException e) {
            LOG.error("Error Parsing returned cache date..no caching will occur", e);
          }
        }
      } else {
        throw new RuntimeException("Error Getting Products....");
      }
    }

    return productList;
  }
}
