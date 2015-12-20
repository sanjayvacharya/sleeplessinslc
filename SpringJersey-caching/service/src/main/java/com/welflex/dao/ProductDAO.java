package com.welflex.dao;

import java.util.Set;

import com.welflex.model.Product;

/**
 * Product Data Access Object definition.
 * 
 * @author Sanjay Acharya
 */
public interface ProductDAO {
  /**
   * @return Set of Products currently present.
   */
  public Set<Product> getProducts();

  /**
   * Gets a Specific product by <code>id</code>
   * 
   * @param id Product id
   * @return Product of a specific id.
   */
  public Product getProduct(Long id);
}
