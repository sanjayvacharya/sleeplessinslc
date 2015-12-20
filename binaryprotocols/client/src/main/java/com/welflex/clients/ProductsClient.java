package com.welflex.clients;

import org.apache.commons.httpclient.HttpClient;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.welflex.order.dto.ProductDto;
import com.welflex.order.dto.ProductListDto;
import com.welflex.products.proto.dto.ProductProtos.ProductList;
import com.welflex.provider.AlternateMediaType;
import com.welflex.provider.ProtobufMessageReader;
import com.welflex.provider.ProtobufMessageWriter;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;

public class ProductsClient {
  private final String baseUri;

  private final ApacheHttpClient client;

  public ProductsClient(String baseUri) {
    this.baseUri = baseUri;    
    ClientConfig cc = new DefaultClientConfig();
    cc.getClasses().add(ProtobufMessageReader.class);
    cc.getClasses().add(ProtobufMessageWriter.class);
    ApacheHttpClientHandler handler = new ApacheHttpClientHandler(new HttpClient());
    client = new ApacheHttpClient(handler, cc);    
  }
  
  public ProductList getProductList() {
    return client.resource(baseUri).path("/products").accept(AlternateMediaType.APPLICATION_XPROTOBUF)
      .get(ProductList.class);
  }
  
  public Set<ProductDto> getProductsXml() {
    ProductListDto prodListDto = client.resource(baseUri).path("/products").accept(
      MediaType.APPLICATION_XML).get(ProductListDto.class);
    return new HashSet<ProductDto>(prodListDto.getProductDTOs());
  }
  
  public Set<ProductDto> getProductsJson() {
    ProductListDto prodListDto = client.resource(baseUri).path("/products").accept(
      MediaType.APPLICATION_JSON).get(ProductListDto.class);
    return new HashSet<ProductDto>(prodListDto.getProductDTOs());
  }
  
  private static void timedRun(String task, Runnable runnable) {
    long start = System.currentTimeMillis();
    runnable.run();
    long end = System.currentTimeMillis();
    System.out.println(task + " Total:" + (end - start));
  }
  
  public static void main(String args[]) {
    final ProductsClient p = new ProductsClient("http://localhost:8080/webapp");
    timedRun("Proto", new Runnable() {
      public void run() {
        p.getProductList();    
      }
    });
    timedRun("XML", new Runnable() {
      public void run() {
        p.getProductsXml();    
      }
    });
    timedRun("Json", new Runnable() {
      public void run() {
        p.getProductsJson();    
      }
    });
    System.out.println("Using xml:" + p.getProductsXml());
    System.out.println("Using json:" + p.getProductsJson());    
  }
}
