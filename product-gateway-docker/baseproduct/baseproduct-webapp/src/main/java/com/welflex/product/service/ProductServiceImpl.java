package com.welflex.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.welflex.product.model.Product;
import com.welflex.product.model.ProductOption;

@Service
public class ProductServiceImpl implements ProductService {
  private final Map<Long, Product> productMap;
  
  public ProductServiceImpl() {
    this.productMap = Maps.newHashMap();
    List<ProductOption> productOptions = Lists.newArrayList();
    productOptions.add(new ProductOption(1L, "Large"));
    productOptions.add(new ProductOption(2L, "Medium"));
    productOptions.add(new ProductOption(3L, "Small"));
    productOptions.add(new ProductOption(4L, "XLarge"));    
    Product product = new Product(9310301L, "Brio Milano Men's Blue and Grey Plaid Button-down Fashion Shirt", "http://ak1.ostkcdn.com/images/products/9310301/Brio-Milano-Mens-Blue-GrayWhite-Black-Plaid-Button-Down-Fashion-Shirt-6ace5a36-0663-4ec6-9f7d-b6cb4e0065ba_600.jpg", 
      productOptions);
    
    productMap.put(product.getProductId(), product);
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
