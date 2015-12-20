package com.welflex.exception;

/**
 * Order Exception.
 * 
 * @author Sanjay Acharya
 */
public class OrderException extends Exception {
  private static final long serialVersionUID = -7466737637950908872L;

  public OrderException(String message) {
    super(message);
  }
}
