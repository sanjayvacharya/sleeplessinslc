package com.welflex.product.service;

import rx.Observable;

import com.overstock.product.dto.Product;

public interface ProductService {
  Observable<Product> getProduct(Long productId);
}
