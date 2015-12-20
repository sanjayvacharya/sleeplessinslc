package com.welflex.exception;

public class OrderException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public OrderException(String message) {
    super(message);
  }
  
  public OrderException(String message, Throwable t) {
    super(message, t);
  }
}
