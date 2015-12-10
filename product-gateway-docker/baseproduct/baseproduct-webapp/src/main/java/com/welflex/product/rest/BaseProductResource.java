package com.welflex.product.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.welflex.product.dto.BaseProduct;
import com.welflex.product.dto.BaseProductOption;
import com.welflex.product.service.ProductService;

@RestController
public class BaseProductResource {
  
  private final ProductService productService;
  
  @Inject
  public BaseProductResource(ProductService productService) {
    this.productService = productService;
  }
  

  @RequestMapping(value = "/baseProduct/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public BaseProduct get(@PathVariable Long id) {

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
