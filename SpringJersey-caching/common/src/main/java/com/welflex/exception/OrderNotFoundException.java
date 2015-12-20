package com.welflex.exception;

/**
 * Exception thrown when an order could not be retrieved.
 * 
 * @author Sanjay Acharya
 */
public class OrderNotFoundException extends OrderException {
  private static final long serialVersionUID = 7435087356659934123L;

  public OrderNotFoundException(String message) {
    super(message);
  }
}
