package com.welflex.product.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.glassfish.jersey.client.rx.rxjava.RxObservable;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.overstock.inventory.dto.ProductInventory;
import com.overstock.price.dto.ProductPrice;
import com.overstock.product.dto.BaseProduct;
import com.overstock.product.dto.BaseProductOption;
import com.overstock.product.dto.Product;
import com.overstock.product.dto.ProductOption;
import com.welflex.product.ServiceUrls;
import com.welflex.review.dto.Reviews;

@Path("/products/{id}/observable")
public class ObservableProductResource {  
  
  private Client client;
  private ServiceUrls serviceUrls;

  @Inject
  public ObservableProductResource(Client client, ServiceUrls serviceUrls) {
    this.client = client;
    this.serviceUrls = serviceUrls;
  }
  
  @GET
  public void observableProduct(@PathParam("id") final Long productId,
    @Suspended final AsyncResponse response) {

    // An Observable of a Result Product from 
    Observable<Product> product = Observable.zip(baseProduct(productId), reviews(productId),
      new Func2<BaseProduct, Reviews, Product>() {

        @Override
        public Product call(BaseProduct product, Reviews review) {
          return resultProductFrom(product, review);
        }
      });

    // All Product
    Observable<Long> productIds = productAndOptionIds(product);
    
    // Observable of Options only
    Observable<Long> optionIds = productIds.filter(new Func1<Long, Boolean>() {

      @Override
      public Boolean call(Long prodId) {
        return !prodId.equals(productId);
      }

    });
    
    // Set Inventory Data
    product
        .zipWith(inventories(productId, optionIds).toList(), new Func2<Product, List<ProductInventory>, Product>() {

          @Override
          public Product call(Product resultProduct, List<ProductInventory> productInventories) {
            for (ProductInventory inventory : productInventories) {
              if (!inventory.getProductId().equals(resultProduct.getProductId())) {
                resultProduct.getOption(inventory.getProductId())
                    .setInventory(inventory.getCount());
              }
            }
            return resultProduct;
          }
        })
        // Set Price Data
        .zipWith(prices(productIds).toList(), new Func2<Product, List<ProductPrice>, Product>() {

          @Override
          public Product call(Product resultProduct, List<ProductPrice> prices) {
            for (ProductPrice price : prices) {
              if (price.getProductId().equals(resultProduct.getProductId())) {
                resultProduct.setPrice(price.getPrice());
              }
              else {
                resultProduct.getOption(price.getProductId()).setPrice(price.getPrice());
              }
            }
            return resultProduct;
          }
        }).observeOn(Schedulers.io()).subscribe(new Action1<Product>() {

          @Override
          public void call(Product productToSet) {
            response.resume(productToSet);
          }

        }, new Action1<Throwable>() {

          @Override
          public void call(Throwable t1) {
            response.resume(t1);
          }
        });
  }
  
  private Observable<BaseProduct> baseProduct(Long productId) {
    return RxObservable
    .from(
      client.target(serviceUrls.getBaseProductUrl()).path("/products").path(String.valueOf(productId)))
    .request().rx().get(BaseProduct.class);
  }
  
  private Observable<Reviews> reviews(Long productId) {
    return RxObservable
        .from(client.target(serviceUrls.getReviewsUrl()).path("/productReviews").path(String.valueOf(productId)))
        .request().rx().get(Reviews.class);
  }
  
  private Observable<Long> productAndOptionIds(Observable<Product> product) {
    return product.flatMap(new Func1<Product, Observable<Long>>() {

      @Override
      public Observable<Long> call(Product resultProduct) {
        return Observable.from(Iterables.concat(
          Lists.<Long> newArrayList(resultProduct.getProductId()),
          Iterables.transform(resultProduct.getOptions(), new Function<ProductOption, Long>() {

            @Override
            public Long apply(ProductOption option) {
              return option.getProductId();
            }
          })));
      }
    });
  }
  
  /**
   * Inventories returns back inventories of the Primary product and options. 
   * However, for the primary product, no web service call is invoked as inventory of the main product is the sum of 
   * inventories of all options. However, a dummy ProductInventory is created to maintain order during final concatenation.
   *
   * @param productId Id of the Product
   * @param optionIds Observable of OptionIds
   * @return An Observable of Product Inventory
   */
  private Observable<ProductInventory> inventories(Long productId, Observable<Long> optionIds) {
    return Observable.just(new ProductInventory(productId, 0))
        .concatWith(optionIds.flatMap(new Func1<Long, Observable<ProductInventory>>() {

      @Override
      public Observable<ProductInventory> call(Long optionId) {
        return RxObservable
            .from(client.target(serviceUrls.getInventoryUrl()).path("/productInventory").path("/{productId}"))
            .resolveTemplate("productId", optionId).request().rx().get(ProductInventory.class);
      }
    }));
  }
  
  private Observable<ProductPrice> prices(Observable<Long> productIds) {
    return productIds
        .flatMap(new Func1<Long, Observable<ProductPrice>>() {

          @Override
          public Observable<ProductPrice> call(Long productId) {
            return RxObservable
                .from(client.target(serviceUrls.getPriceUrl()).path("/productPrice").path("/{productId}"))
                .resolveTemplate("productId", productId.toString()).request().rx()
                .get(ProductPrice.class);
          }
        });
  }

  /**
   * Create a Result Product
   * 
   * @param product Base Product
   * @param reviews Reviews
   * @return A Base Product
   */
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
