package com.welflex.product.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import rx.Observable;
import rx.functions.Func2;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.overstock.inventory.dto.ProductInventory;
import com.overstock.price.dto.ProductPrice;
import com.overstock.product.dto.BaseProduct;
import com.overstock.product.dto.BaseProductOption;
import com.overstock.product.dto.Product;
import com.overstock.product.dto.ProductOption;
import com.welflex.product.client.BaseProductClient;
import com.welflex.product.client.InventoryClient;
import com.welflex.product.client.PriceClient;
import com.welflex.product.client.ReviewsClient;
import com.welflex.review.dto.Reviews;

@Service
public class ProductServiceImpl implements ProductService {
  private final BaseProductClient baseProductClient;

  private final InventoryClient inventoryClient;

  private final PriceClient priceClient;

  private final ReviewsClient reviewsClient;

  @Inject
  public ProductServiceImpl(BaseProductClient baseProductClient, InventoryClient inventoryClient,
      PriceClient priceClient, ReviewsClient reviewsClient) {
    this.baseProductClient = baseProductClient;
    this.inventoryClient = inventoryClient;
    this.priceClient = priceClient;
    this.reviewsClient = reviewsClient;
  }

  private Observable<BaseProduct> baseProduct(final Long productId) {
    return Observable.defer(() -> Observable.just(baseProductClient.getBaseProduct(productId)));
  }

  private Observable<ProductPrice> price(final Long productId) {
    return Observable.defer(() -> Observable.just(priceClient.getPrice(productId)));
  }

  private Observable<ProductInventory> inventory(final Long productId) {
    return Observable.defer(() -> Observable.just(inventoryClient.getInventory(productId)));
  }

  private Observable<Reviews> reviews(final Long productId) {
    return Observable.defer(() -> Observable.just(reviewsClient.getReviews(productId)));
  }

  @Override
  public Observable<Product> getProduct(Long productId) {
    // An Observable of a Result Product from
    Observable<Product> product = Observable.zip(baseProduct(productId), reviews(productId), (
      product1, review) -> resultProductFrom(product1, review));

    // All Product
    Observable<Long> productIds = productAndOptionIds(product);

    // Observable of Options only
    Observable<Long> optionIds = productIds.filter(prodId -> !prodId.equals(productId));

    // Set Inventory Data
    return product.zipWith(inventories(productId, optionIds).toList(),
      (Func2<Product, List<ProductInventory>, Product>) (resultProduct, productInventories) -> {
        for (ProductInventory inventory : productInventories) {
          if (!inventory.getProductId().equals(resultProduct.getProductId())) {
            resultProduct.getOption(inventory.getProductId()).setInventory(inventory.getCount());
          }
        }
        return resultProduct;
      })
    // Set Price Data
        .zipWith(prices(productIds).toList(),
          (Func2<Product, List<ProductPrice>, Product>) (resultProduct, prices) -> {
            for (ProductPrice price : prices) {
              if (price.getProductId().equals(resultProduct.getProductId())) {
                resultProduct.setPrice(price.getPrice());
              }
              else {
                resultProduct.getOption(price.getProductId()).setPrice(price.getPrice());
              }
            }
            return resultProduct;
          });
  }

  private Observable<Long> productAndOptionIds(Observable<Product> product) {
    return product.flatMap(resultProduct -> Observable.from(Iterables.concat(
      Lists.<Long> newArrayList(resultProduct.getProductId()),
      Iterables.<ProductOption, Long>transform(resultProduct.getOptions(), option -> option.getProductId()))));
  }

  /**
   * Inventories returns back inventories of the Primary product and options. However, for the
   * primary product, no web service call is invoked as inventory of the main product is the sum of
   * inventories of all options. However, a dummy ProductInventory is created to maintain order
   * during final concatenation.
   *
   * @param productId Id of the Product
   * @param optionIds Observable of OptionIds
   * @return An Observable of Product Inventory
   */
  private Observable<ProductInventory> inventories(Long productId, Observable<Long> optionIds) {
    return Observable.just(new ProductInventory(productId, 0)).concatWith(
      optionIds.flatMap(optionId -> inventory(optionId)));
  }

  private Observable<ProductPrice> prices(Observable<Long> productIds) {
    return productIds.flatMap(productId -> price(productId));
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
