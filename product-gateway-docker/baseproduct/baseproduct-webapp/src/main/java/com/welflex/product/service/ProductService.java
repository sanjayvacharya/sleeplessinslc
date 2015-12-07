package com.welflex.product.service;

import java.util.List;

import com.welflex.product.model.Product;
import com.welflex.product.model.ProductOption;

public interface ProductService {
  Product  getProduct(Long productId);
  List<ProductOption> getOption(Long productId, Long optionId);
}
