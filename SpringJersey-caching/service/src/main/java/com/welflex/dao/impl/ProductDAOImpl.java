package com.welflex.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.welflex.dao.ProductDAO;
import com.welflex.model.Product;

@Repository public class ProductDAOImpl implements ProductDAO {
  private Map<Long, Product> productMap = new HashMap<Long, Product>();

  public ProductDAOImpl() {
    Product p = new Product();
    p.setId(new Long(2313));
    p.setName("XBOX");
    p.setDescription("XBox Gaming Console Halo Edition");

    productMap.put(p.getId(), p);

    p = new Product();

    p.setId(new Long(663123));
    p.setName("Samsung Plasma 42");
    p.setDescription("42 inch Samsung Plasma with XBrite");

    productMap.put(p.getId(), p);

    p = new Product();

    p.setId(new Long(9912123));
    p.setName("HDMI Cable");
    p.setDescription("Gold Plated HDMI Cable.");

    productMap.put(p.getId(), p);
  }

  public Product getProduct(Long id) {
    return productMap.get(id);
  }

  public Set<Product> getProducts() {
    return new HashSet<Product>(productMap.values());
  }
}
