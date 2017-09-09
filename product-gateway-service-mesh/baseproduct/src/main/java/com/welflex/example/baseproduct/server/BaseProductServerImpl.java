package com.welflex.example.baseproduct.server;

import java.util.List;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;

import com.google.common.collect.Lists;
import com.welflex.baseproduct.generated.BaseProduct;
import com.welflex.baseproduct.generated.BaseProductCore;
import com.welflex.baseproduct.generated.BaseProductOption;
import com.welflex.baseproduct.generated.BaseProductServiceGrpc.BaseProductServiceImplBase;
import com.welflex.baseproduct.generated.BaseProductResponse;

@GRpcService
public class BaseProductServerImpl extends BaseProductServiceImplBase {
  private static final Logger LOGGER = Logger.getLogger(BaseProductServerImpl.class);

  @Override
  public void getBaseProduct(com.welflex.baseproduct.generated.BaseProductRequest request,
    io.grpc.stub.StreamObserver<com.welflex.baseproduct.generated.BaseProductResponse> responseObserver) {
	LOGGER.info("Requested Base Product with Id:" + request.getProductId());
    List<BaseProductOption> options = Lists.newArrayList();

    // Code far better than this please :-)
    options
        .add(BaseProductOption.newBuilder().setProductId(1).setOptionDescription("Large").build());
    options
        .add(BaseProductOption.newBuilder().setProductId(2).setOptionDescription("Medium").build());
    options
        .add(BaseProductOption.newBuilder().setProductId(3).setOptionDescription("Small").build());
    options
        .add(BaseProductOption.newBuilder().setProductId(4).setOptionDescription("XLarge").build());
    
    BaseProductCore baseProductCore = BaseProductCore.newBuilder()
        .setDescription("Brio Milano Men's Blue and Grey Plaid Button-down Fashion Shirt")
        .setImageUrl(
          "http://ak1.ostkcdn.com/images/products/9310301/Brio-Milano-Mens-Blue-GrayWhite-Black-Plaid-Button-Down-Fashion-Shirt-6ace5a36-0663-4ec6-9f7d-b6cb4e0065ba_600.jpg")
        .setProductId(9310301).build();
   
    responseObserver
        .onNext(BaseProductResponse.newBuilder().setBaseProduct(BaseProduct.newBuilder().setBaseProductCore(baseProductCore)
          .addAllOptions(options)).build());
    LOGGER.info("Responsed with Base Product for Id:" + request.getProductId());
    
    responseObserver.onCompleted();
  }
}
