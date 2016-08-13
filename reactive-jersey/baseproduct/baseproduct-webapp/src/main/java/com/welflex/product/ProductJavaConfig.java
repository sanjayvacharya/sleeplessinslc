package com.welflex.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.welflex.product.model.Product;
import com.welflex.product.model.ProductOption;
import com.welflex.product.service.ProductService;
import com.welflex.product.service.ProductServiceImpl;

@Configuration
public class ProductJavaConfig {
  @Bean
  public ProductService productService() {
    Map<Long, Product> productMap = new HashMap<>();
    List<ProductOption> productOptions = Lists.newArrayList();
    productOptions.add(new ProductOption(1L, "Large"));
    productOptions.add(new ProductOption(2L, "Medium"));
    productOptions.add(new ProductOption(3L, "Small"));
    productOptions.add(new ProductOption(4L, "XLarge"));    
    Product product = new Product(9310301L, "Brio Milano Men's Blue and Grey Plaid Button-down Fashion Shirt", "http://ak1.ostkcdn.com/images/products/9310301/Brio-Milano-Mens-Blue-GrayWhite-Black-Plaid-Button-Down-Fashion-Shirt-6ace5a36-0663-4ec6-9f7d-b6cb4e0065ba_600.jpg", 
      productOptions);
    
    productMap.put(product.getProductId(), product);
    ProductServiceImpl impl = new ProductServiceImpl(productMap);
    return impl;
  }  
}
