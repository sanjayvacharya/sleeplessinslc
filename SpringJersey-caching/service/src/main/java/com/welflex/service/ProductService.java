package com.welflex.service;

import java.util.Set;

import com.welflex.model.Product;

public interface ProductService {
  /**
   * @return A set of Products
   */
  public Set<Product> getProducts();
}
