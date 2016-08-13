package com.welflex.product.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.welflex.product.model.Product;
import com.welflex.product.model.ProductOption;

public class ProductServiceImpl implements ProductService {
  private final Map<Long, Product> productMap;
  
  public ProductServiceImpl(Map<Long, Product> productMap) {
    this.productMap = productMap;
  }
  
  @Override
  public Product getProduct(Long productId) {
    try {
      Thread.sleep(50);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return productMap.get(productId);
  }

  @Override
  public List<ProductOption> getOption(Long productId, Long optionId) {    
    return ImmutableList.<ProductOption>builder().addAll(productMap.get(productId).getOptions()).build();
  }
}
