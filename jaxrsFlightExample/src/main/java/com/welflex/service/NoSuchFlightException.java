package com.welflex.service;

public class NoSuchFlightException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchFlightException(String message) {
    super(message);
  }
}
