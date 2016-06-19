package com.welflex.exception;

/**
 * Thrown when a conflict is detected either due to optimistic or
 * pessimistic locking.
 *
 */
public class StaleObjectException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public StaleObjectException(String message) {
    super(message);
  }
  
  public StaleObjectException(String message, Throwable cause) {
    super(message, cause);
  }
}
