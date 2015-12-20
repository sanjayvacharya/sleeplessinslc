package com.welflex.exception;

public class OrderValidationException extends OrderException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public OrderValidationException() {
   super("Error validating the order");
  }

  public OrderValidationException(String message) {
    super(message);    
  }

}
