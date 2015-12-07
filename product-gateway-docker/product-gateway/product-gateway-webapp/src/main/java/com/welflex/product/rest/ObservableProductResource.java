package com.welflex.product.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.schedulers.Schedulers;

import com.overstock.product.dto.Product;
import com.welflex.product.service.ProductService;

@RestController
public class ObservableProductResource {  
  private ProductService productService;

  @Inject
  public ObservableProductResource(ProductService productService) {
    this.productService = productService;
  }
  
  @RequestMapping("/products/{productId}")
  public DeferredResult<Product> get(@PathVariable Long productId) {
    DeferredResult<Product> d = new DeferredResult<Product>();
  
    productService.getProduct(productId).observeOn(Schedulers.io())
    .subscribe(productToSet -> d.setResult(productToSet), t1 -> d.setErrorResult(t1));
    
    return d;
  }
  
}
