package com.welflex.product.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.common.collect.Lists;
import com.overstock.product.dto.BaseProduct;
import com.overstock.product.dto.BaseProductOption;
import com.welflex.product.service.ProductService;

@Path("/products/{id}")
public class BaseProductResource {
  
  private final ProductService productService;
  
  @Inject
  public BaseProductResource(ProductService productService) {
    this.productService = productService;
  }
  
  @GET
  public BaseProduct get(@PathParam("id") Long id) {

    com.welflex.product.model.Product product = productService.getProduct(id);
    BaseProduct dto = new BaseProduct();
   
    dto.setProductId(id);
    dto.setImageUrl(product.getImageUrl());
    dto.setDescription(product.getDescription());
    
    List<BaseProductOption> productOptions = Lists.newArrayList();
    
    for (com.welflex.product.model.ProductOption option : product.getOptions()) {
      productOptions.add(new BaseProductOption(option.getProductId(), option.getOptionDescription()));
    }
    
    dto.setOptions(productOptions);
    
    return dto;
  }
}
