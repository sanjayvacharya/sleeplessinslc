package com.welflex.order.rest.resource;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.dozer.util.mapping.MapperIF;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.welflex.model.Product;
import com.welflex.order.dto.ProductDto;
import com.welflex.order.dto.ProductListDto;
import com.welflex.service.ProductService;

/**
 * Product Resource
 * Maintainence: I had to use the @Component tag here. Should not be necessary
 * as per the documentation.
 *
 * @author Sanjay Acharya
 */
@Component
@Path("/products")
@Scope("singleton")
public class ProductsResource {
  private static final Logger log = Logger.getLogger(ProductsResource.class);

  @Autowired private ProductService productService;

  @Autowired private MapperIF beanMapper;

  private Set<ProductDto> map(Set<Product> products) {
    Set<ProductDto> productDtos = new HashSet<ProductDto>();

    for (Product product : products) {
      ProductDto productDto = (ProductDto) beanMapper.map(product, ProductDto.class);
      productDtos.add(productDto);
    }

    return productDtos;
  }

  /**
   * Gets a {@link ProductListDto} of Products that are supported.
   *
   * @return a List of Products
   */
  @GET @Produces("application/json") public Response getProducts() {
    log.debug("Enter getProducts()");

    Set<Product> products = productService.getProducts();
    Set<ProductDto> productDtos = map(products);

    ProductListDto productListDto = new ProductListDto(productDtos);
    Response.ResponseBuilder response = Response.ok(productListDto).type(MediaType.APPLICATION_JSON);

    Date expirationDate = new Date(System.currentTimeMillis() + 3000);

    response.expires(expirationDate);

    return response.build();
  }
}
