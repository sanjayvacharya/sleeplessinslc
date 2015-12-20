package com.welflex.service;

public class NoSeatAvailableException extends Exception {
  private static final long serialVersionUID = 1L;

  public NoSeatAvailableException(String message) {
    super(message);
  }
}
