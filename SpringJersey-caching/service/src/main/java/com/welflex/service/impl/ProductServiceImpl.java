package com.welflex.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welflex.dao.ProductDAO;
import com.welflex.model.Product;
import com.welflex.service.ProductService;

@Service public class ProductServiceImpl implements ProductService {
  @Autowired private ProductDAO productDAO;

  public Set<Product> getProducts() {
    return productDAO.getProducts();
  }
}
