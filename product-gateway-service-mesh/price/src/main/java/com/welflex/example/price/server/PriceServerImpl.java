package com.welflex.example.price.server;

import java.util.Map;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.welflex.price.generated.PriceResponse;
import com.welflex.price.generated.ProductPrice;
import com.welflex.price.generated.PriceServiceGrpc.PriceServiceImplBase;

@GRpcService
@RestController
public class PriceServerImpl extends PriceServiceImplBase {
  private static final Map<Integer, Double> priceMap = Maps.newHashMap();
  static {
    priceMap.put(1, new Double(39.99));
    priceMap.put(2, new Double(32.99));
    priceMap.put(3, new Double(39.99));
    priceMap.put(4, new Double(39.99));
    priceMap.put(9310301, new Double(38.99));
  }
  
  public void get(com.welflex.price.generated.PriceRequest request,
    io.grpc.stub.StreamObserver<com.welflex.price.generated.PriceResponse> responseObserver) {
    responseObserver.onNext(PriceResponse.newBuilder()
      .setProductPrice(ProductPrice.newBuilder().setProductId(request.getProductId())
        .setPrice(priceMap.get(request.getProductId())).build()).build());            
    responseObserver.onCompleted();
  }
  
  @RequestMapping(value = "/productPrice/{productId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public String price(@PathVariable("productId") Integer productId) {
    return String.valueOf(priceMap.get(productId));
  }
}
