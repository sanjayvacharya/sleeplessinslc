package com.welflex.service;

public class NoSuchAirportException extends Exception {
  private static final long serialVersionUID = 1L;

  public NoSuchAirportException(String message) {
    super(message);
  }
}
