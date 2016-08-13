package com.welflex.product.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.overstock.inventory.dto.ProductInventory;
import com.overstock.price.dto.ProductPrice;
import com.overstock.product.dto.BaseProduct;
import com.overstock.product.dto.BaseProductOption;
import com.overstock.product.dto.Product;
import com.overstock.product.dto.ProductOption;
import com.welflex.product.ServiceUrls;
import com.welflex.review.dto.Reviews;

@Path("/products/{id}")
public class DefaultProductResource {

  private Client client;

  private ServiceUrls serviceUrls;

  @Inject
  public DefaultProductResource(Client client, ServiceUrls serviceUrls) {
    this.client = client;
    this.serviceUrls = serviceUrls;
  }

  @GET
  public void observableProduct(@PathParam("id") Long productId,
    @Suspended final AsyncResponse response) {
    try {
      // Get the Product
      BaseProduct baseProduct = client.target(serviceUrls.getBaseProductUrl()).path("/products")
          .path(String.valueOf(productId)).request().get(BaseProduct.class);

      // Get reviews of the Product
      Reviews reviews = client.target(serviceUrls.getReviewsUrl()).path("/productReviews")
          .path(String.valueOf(productId)).request().get(Reviews.class);

      // Create a Result Product
      Product resultProduct = resultProductFrom(baseProduct, reviews);

      // Obtain the Price for Result Product
      resultProduct.setPrice(client.target(serviceUrls.getPriceUrl()).path("/productPrice")
          .path(productId.toString()).request().get(ProductPrice.class).getPrice());

      // Get Price and Inventory of Product and set it on the Option
      for (Long optionId : optionId(resultProduct.getOptions())) {        
        Double price = client.target(serviceUrls.getPriceUrl()).path("/productPrice")
            .path(optionId.toString()).request().get(ProductPrice.class).getPrice();

        ProductInventory inventory = client.target(serviceUrls.getInventoryUrl())
            .path("/productInventory").path(optionId.toString()).request()
            .get(ProductInventory.class);

        ProductOption option = resultProduct.getOption(optionId);

        option.setInventory(inventory.getCount());
        option.setPrice(price);
   
      }

      response.resume(resultProduct);
    }
    catch (Exception e) {
      response.resume(e);
    }
  }

  private Iterable<Long> optionId(List<ProductOption> options) {
    return Iterables.transform(options, new Function<ProductOption, Long>() {

      @Override
      public Long apply(ProductOption option) {
        return option.getProductId();
      }
    });
  }

  private Product resultProductFrom(BaseProduct product, Reviews reviews) {
    Product resultProduct = new Product();

    resultProduct.setProductId(product.getProductId());
    resultProduct.setProductId(product.getProductId());
    resultProduct.setDescription(product.getDescription());
    resultProduct.setImageUrl(product.getImageUrl());

    for (BaseProductOption productOption : product.getOptions()) {
      ProductOption option = new ProductOption();

      option.setProductId(productOption.getProductId());
      option.setOptionDescription(productOption.getOptionDescription());

      resultProduct.addOption(option);
    }

    resultProduct.setReviews(reviews.getReviews());

    return resultProduct;
  }
}
