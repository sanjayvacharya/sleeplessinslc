package com.welflex.order.rest.resource;

import java.util.ArrayList;
import java.util.List;
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
import com.welflex.order.rest.util.Utils;
import com.welflex.products.proto.dto.ProductProtos.ProductList;
import com.welflex.provider.AlternateMediaType;
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
  
  /**
   *
   * @return a List of Products
   */
  @GET 
  @Produces(AlternateMediaType.APPLICATION_XPROTOBUF) 
  public Response getProducts() {
    log.debug("Enter getProducts()");
    Set<Product> products = productService.getProducts();
    ProductList productList = Utils.build(products);

    return Response.ok(productList).type(AlternateMediaType.APPLICATION_XPROTOBUF).build();
  }

  @Autowired private MapperIF beanMapper;

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getProductsAsXml() {
    return Response.ok(getProductListDtos()).type(MediaType.APPLICATION_XML).build();
  }
  
  private ProductListDto getProductListDtos() {
    Set<Product> products = productService.getProducts();
    List<ProductDto> productDtos = new ArrayList<ProductDto>();

    for (Product product : products) {
      ProductDto productDto = (ProductDto) beanMapper.map(product, ProductDto.class);
      productDtos.add(productDto);
    }

    ProductListDto productListDto = new ProductListDto();
    productListDto.setProductDTOs(productDtos);
    
    return productListDto;
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductsAsJson() {
    log.debug("Enter getProducts() as JSON");
    return Response.ok(getProductListDtos()).type(MediaType.APPLICATION_JSON).build();
  }
}
