package me.hsgamer.hscore.expansion.common;

/**
 * The exception that is thrown when the addon is invalid
 */
public class InvalidExpansionException extends RuntimeException {
  public InvalidExpansionException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidExpansionException(String message) {
    super(message);
  }
}
