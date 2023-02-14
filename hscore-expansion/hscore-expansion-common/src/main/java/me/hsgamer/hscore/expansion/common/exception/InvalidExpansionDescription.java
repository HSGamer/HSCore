package me.hsgamer.hscore.expansion.common.exception;

/**
 * The exception that is thrown when the addon description is invalid
 */
public class InvalidExpansionDescription extends Exception {
  public InvalidExpansionDescription(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidExpansionDescription(String message) {
    super(message);
  }
}
