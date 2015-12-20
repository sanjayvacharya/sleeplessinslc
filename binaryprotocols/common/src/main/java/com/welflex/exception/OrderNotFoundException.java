package com.welflex.exception;

public class OrderNotFoundException extends OrderException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public OrderNotFoundException(String message) {
    super(message);
  }
}
