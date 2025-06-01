package com.codewithmosh.store.invitation;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException() {
    super("Invalid token");
  }
}
