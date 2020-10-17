package com.dimediary.exceptions;

public class RecurrenceException extends RuntimeException {

  private static final long serialVersionUID = -2915648219392421762L;

  public RecurrenceException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
