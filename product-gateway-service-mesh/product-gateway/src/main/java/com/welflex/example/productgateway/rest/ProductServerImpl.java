package com.welflex.example.productgateway.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.welflex.baseproduct.generated.BaseProduct;
import com.welflex.baseproduct.generated.BaseProductOption;
import com.welflex.baseproduct.generated.BaseProductRequest;
import com.welflex.baseproduct.generated.BaseProductResponse;
import com.welflex.baseproduct.generated.BaseProductServiceGrpc;
import com.welflex.baseproduct.generated.BaseProductServiceGrpc.BaseProductServiceBlockingStub;
import com.welflex.inventory.generated.InventoryRequest;
import com.welflex.inventory.generated.InventoryServiceGrpc;
import com.welflex.inventory.generated.ProductInventory;
import com.welflex.price.generated.PriceRequest;
import com.welflex.price.generated.PriceServiceGrpc;
import com.welflex.price.generated.ProductPrice;
import com.welflex.product.generated.ProductResponse;
import com.welflex.product.generated.ProductServiceGrpc.ProductServiceImplBase;
import com.welflex.reviews.generated.ProductReview;
import com.welflex.reviews.generated.ReviewsRequest;
import com.welflex.reviews.generated.ReviewsResponse;
import com.welflex.reviews.generated.ReviewsServiceGrpc;
import com.welflex.reviews.generated.ReviewsServiceGrpc.ReviewsServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@GRpcService
@RestController
public class ProductServerImpl extends ProductServiceImplBase {
  private static final Logger LOGGER = Logger.getLogger(ProductServerImpl.class);

  @Value("${BASEPRODUCT_SERVICE_HOST:localhost}")
  private String baseProductServiceHost;

  @Value("${BASEPRODUCT_SERVICE_PORT:6565}")
  private int baseProductServicePort;

  @Value("${INVENTORY_SERVICE_HOST:localhost}")
  private String inventoryServiceHost;

  @Value("${INVENTORY_SERVICE_PORT:6566}")
  private int inventoryServicePort;

  @Value("${PRICE_SERVICE_HOST:localhost}")
  private String priceServiceHost;

  @Value("${PRICE_SERVICE_PORT:6567}")
  private int priceServicePort;

  @Value("${REVIEWS_SERVICE_HOST:localhost}")
  private String reviewsServiceHost;

  @Value("${REVIEWS_SERVICE_PORT:6568}")
  private int reviewsServicePort;

  @Override
  public void get(com.welflex.product.generated.ProductRequest request,
    io.grpc.stub.StreamObserver<com.welflex.product.generated.ProductResponse> responseObserver) {
    responseObserver.onNext(ProductResponse.newBuilder().setProduct(getProduct(request.getProductId())).build());
    responseObserver.onCompleted();
  }
  
  private com.welflex.product.generated.Product getProduct(int productId) {
	LOGGER.info("Requesting BaseProduct for ProductId:" + productId);
	BaseProduct baseProduct = getBaseProduct(productId);
	LOGGER.info("Got BaseProduct for ProductId:" + productId);
	
    List<com.welflex.product.generated.ProductOption> options = Lists.newArrayList();
    
    for (BaseProductOption o : baseProduct.getOptionsList()) {
      LOGGER.info("Requesting Inventory for Option Id:" + o.getProductId());
      ProductInventory inventory = getProductInventory(o.getProductId());
      LOGGER.info("Received Inventory for Option Id:" + o.getProductId());
      LOGGER.info("Requesting Price for Option Id:" + o.getProductId());
      
      ProductPrice productPrice = getProductPrice(o.getProductId());
      LOGGER.info("Received Prices for Option Id:" + o.getProductId());
      
      options.add(com.welflex.product.generated.ProductOption.newBuilder().setBaseProductOption(o)
        .setProductInventory(inventory).setProductPrice(productPrice).build());
    }
    
    LOGGER.info("Requesting Reviews for ProductId:" + productId);
    List<ProductReview> productReviews = getProductReviews(productId);
    LOGGER.info("Received Reviews for ProductId:" + productId);
    
    LOGGER.info("Requesting Price for ProductId:" + productId);
    ProductPrice productPrice = getProductPrice(productId);
    LOGGER.info("Received Price for ProductId:" + productId);
    
    
    return com.welflex.product.generated.Product.newBuilder().setInfo(baseProduct.getBaseProductCore())
        .setProductPrice(productPrice)
      .addAllOptions(options).addAllReviews(productReviews).build();
  }
  
  private BaseProduct getBaseProduct(int productId) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(baseProductServiceHost, baseProductServicePort).usePlaintext(true)
        .build();
    BaseProductServiceBlockingStub baseProductBlockingStub = BaseProductServiceGrpc
        .newBlockingStub(channel);
   
    BaseProductResponse baseProductRespones = baseProductBlockingStub
        .getBaseProduct(BaseProductRequest.newBuilder().setProductId(productId).build());

    return baseProductRespones.getBaseProduct();
  }
  
  private List<ProductReview> getProductReviews(int productId) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(reviewsServiceHost, reviewsServicePort).usePlaintext(true)
        .build();
    
    ReviewsServiceBlockingStub reviewsBlockingStub = ReviewsServiceGrpc.newBlockingStub(channel);
    ReviewsResponse reviewsResponse = reviewsBlockingStub.get(ReviewsRequest.newBuilder().setProductId(productId).build());
    
    return reviewsResponse.getReviewList();
  }
  

  private ProductPrice getProductPrice(int productId) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(priceServiceHost, priceServicePort).usePlaintext(true)
        .build();
    
    return PriceServiceGrpc.newBlockingStub(channel).get(PriceRequest.newBuilder()
      .setProductId(productId).build()).getProductPrice();
  }


  private ProductInventory getProductInventory(int productId) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(inventoryServiceHost, inventoryServicePort).usePlaintext(true)
        .build();
    
    return InventoryServiceGrpc.newBlockingStub(channel).get(InventoryRequest.newBuilder()
      .setProductId(productId).build()).getProductInventory();
  }


  static class Product {
    private Integer productId;

    private String description;

    private String imageUrl;

    private List<ProductOption> options = Lists.newArrayList();

    private List<String> reviews = Lists.newArrayList();

    private Double price;

    public Product() {}

    public ProductOption getOption(Long optionId) {
      for (ProductOption o : options) {
        if (optionId.equals(o.getProductId())) {
          return o;
        }
      }
      return null;
    }

    /**
     * Product's inventory is a sum of inventory of options
     */
    public int getInventory() {
      int inventory = 0;
      for (ProductOption o : options) {
        inventory += o.getInventory();
      }
      return inventory;
    }

    public List<String> getReviews() {
      return reviews;
    }

    public Double getPrice() {
      return price;
    }

    public void setReviews(List<String> reviews) {
      this.reviews = reviews;
    }

    public Product(Integer productId) {
      this.productId = productId;
    }

    public Integer getProductId() {
      return productId;
    }

    public void setProductId(Integer productId) {
      this.productId = productId;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public List<ProductOption> getOptions() {
      return options;
    }

    public void addOption(ProductOption option) {
      options.add(option);
    }

    public void setOptions(List<ProductOption> options) {
      this.options = options;
    }

    public void setPrice(Double price) {
      this.price = price;
    }

    public void addReview(String review) {
      reviews.add(review);
    }
  }

  static class ProductOption {
    private Integer productId;

    private String optionDescription;

    private int inventory;

    private Double price;

    public ProductOption() {}

    public ProductOption(Integer productId, String optionDescription) {
      this.productId = productId;
      this.optionDescription = optionDescription;
    }
    
    public boolean isInStock() {
      return inventory > 0;
    }

    public int getInventory() {
      return inventory;
    }

    public Double getPrice() {
      return price;
    }

    public Integer getProductId() {
      return productId;
    }

    public void setProductId(Integer productId) {
      this.productId = productId;
    }

    public String getOptionDescription() {
      return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
      this.optionDescription = optionDescription;
    }

    public void setInventory(int inventory) {
      this.inventory = inventory;
    }

    public void setPrice(Double price) {
      this.price = price;
    }
  }

  @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Product get(@PathVariable int id) {
	LOGGER.info("Requested Product with Id:" + id);
    Product p = create(id);
    return p;
  }
  
  @RequestMapping(value = "/product/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView getProductPage(@PathVariable int id) {
	ModelAndView mav = new ModelAndView("product");
	mav.addObject("product", create(id));
    return mav;
  }


  private Product create(int id) {
    com.welflex.product.generated.Product productDto = getProduct(id);
    Product product = new Product(id);
    product.setDescription(productDto.getInfo().getDescription());
    product.setImageUrl(productDto.getInfo().getImageUrl());
    
    List<ProductOption> productOptions = Lists.newArrayList();
    
    for (com.welflex.product.generated.ProductOption o : productDto.getOptionsList()) {
      ProductInventory productInventory = o.getProductInventory();
      ProductPrice productPrice = o.getProductPrice();
      ProductOption option = new ProductOption(o.getBaseProductOption().getProductId(), o.getBaseProductOption().getOptionDescription());
      option.setInventory(productInventory.getCount());
      option.setPrice(productPrice.getPrice());
      productOptions.add(option);
    }
   
    product.setOptions(productOptions);
    product.setPrice(productDto.getProductPrice().getPrice());
   
    List<String> reviews = productDto.getReviewsList().stream()
    	.map(t -> t.getReview()).collect(Collectors.toList());
    
    product.setReviews(reviews);
    
    return product;
  }
}
