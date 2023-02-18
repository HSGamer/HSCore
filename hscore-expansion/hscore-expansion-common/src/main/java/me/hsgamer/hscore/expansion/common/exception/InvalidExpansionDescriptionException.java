package me.hsgamer.hscore.expansion.common.exception;

/**
 * The exception when the expansion description is invalid
 */
public class InvalidExpansionDescriptionException extends RuntimeException {
  public InvalidExpansionDescriptionException(String message) {
    super(message);
  }

  public InvalidExpansionDescriptionException(String message, Throwable cause) {
    super(message, cause);
  }
}
