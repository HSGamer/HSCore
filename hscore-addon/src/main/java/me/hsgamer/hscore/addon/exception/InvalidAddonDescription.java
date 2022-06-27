package me.hsgamer.hscore.addon.exception;

/**
 * The exception that is thrown when the addon description is invalid
 */
public class InvalidAddonDescription extends Exception {
  public InvalidAddonDescription(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidAddonDescription(String message) {
    super(message);
  }
}
