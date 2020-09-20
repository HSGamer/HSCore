package me.hsgamer.hscore.addon.exception;

/**
 * A runtime exception for required addon path
 */
public class RequiredAddonPathException extends RuntimeException {

  public RequiredAddonPathException(String message) {
    super(message);
  }
}
