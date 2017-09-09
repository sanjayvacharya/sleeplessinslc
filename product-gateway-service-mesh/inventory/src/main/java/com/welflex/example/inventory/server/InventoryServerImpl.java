package com.welflex.example.inventory.server;

import java.util.Map;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.welflex.inventory.generated.InventoryResponse;
import com.welflex.inventory.generated.InventoryServiceGrpc.InventoryServiceImplBase;
import com.welflex.inventory.generated.ProductInventory;


@GRpcService
@RestController
public class InventoryServerImpl extends InventoryServiceImplBase {
  private static Map<Integer, Integer> productInventory = Maps.newHashMap();

  static {
    productInventory.put(1, 20);
    productInventory.put(2, 32);
    productInventory.put(3, 41);
    productInventory.put(4, 0);
    productInventory.put(9310301, 20 + 32 + 41);
  }

  @Override
  public void get(com.welflex.inventory.generated.InventoryRequest request,
    io.grpc.stub.StreamObserver<com.welflex.inventory.generated.InventoryResponse> responseObserver) {
    responseObserver.onNext(InventoryResponse.newBuilder().setProductInventory(ProductInventory.newBuilder().
      setProductId(request.getProductId()).setCount(productInventory.get(request.getProductId())).build()).build());
    responseObserver.onCompleted();
  }
  
  @RequestMapping(value = "/productInventory/{productId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String get(@PathVariable("productId") Integer productId) {
    return String.valueOf(productInventory.get(productId));
  }
}   
