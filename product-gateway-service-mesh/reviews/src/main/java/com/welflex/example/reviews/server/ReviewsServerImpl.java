package com.welflex.example.reviews.server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.welflex.reviews.generated.ProductReview;
import com.welflex.reviews.generated.ReviewsResponse;
import com.welflex.reviews.generated.ReviewsServiceGrpc.ReviewsServiceImplBase;

@RestController
@GRpcService
public class ReviewsServerImpl extends ReviewsServiceImplBase {
  private static Map<Integer, List<String>> productReviews = Maps.newHashMap();

  static {
	List<String> reviewComments = Lists.newArrayList();
	reviewComments.add("Very Beautiful");
	reviewComments.add("My husband loved it");
	reviewComments.add("This color is horrible for a work place");
	productReviews.put(9310301, reviewComments);
  }

  public void get(com.welflex.reviews.generated.ReviewsRequest request,
      io.grpc.stub.StreamObserver<com.welflex.reviews.generated.ReviewsResponse> responseObserver) {
	List<String> reviews = productReviews.get(request.getProductId());
	if (reviews == null) {
	  reviews = Lists.newArrayList();
	}
	List<ProductReview> productReviews = reviews.stream()
	    .map(t -> ProductReview.newBuilder().setProductId(request.getProductId()).setReview(t).build())
	    .collect(Collectors.toList());

	responseObserver.onNext(ReviewsResponse.newBuilder().addAllReview(productReviews).build());
	responseObserver.onCompleted();
  }

  @RequestMapping(value = "/productReviews/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Reviews productReviews(@PathVariable("productId") Integer productId) {
	List<String> reviews = productReviews.get(productId);
	return new Reviews(productId, reviews);
  }

  static class Reviews {
	private Integer productId;

	private List<String> reviews;

	public Reviews() {
	}

	public Reviews(Integer productId, List<String> reviews) {
	  this.productId = productId;
	  this.reviews = reviews;
	}

	public Integer getProductId() {
	  return productId;
	}

	public void setProductId(Integer productId) {
	  this.productId = productId;
	}

	public List<String> getReviews() {
	  return reviews;
	}

	public void setReviews(List<String> reviews) {
	  this.reviews = reviews;
	}
  }
}
