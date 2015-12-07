package com.welflex.review.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reviews {
  private Long productId;
  
  private List<String> reviews;
  
  public Reviews() {}
  
  public Reviews(Long productId, List<String> reviews) {
    this.productId = productId;
    this.reviews = reviews;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public List<String> getReviews() {
    return reviews;
  }

  public void setReviews(List<String> reviews) {
    this.reviews = reviews;
  }
  
  

}
