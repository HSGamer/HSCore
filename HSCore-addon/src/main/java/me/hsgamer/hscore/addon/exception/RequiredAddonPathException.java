package me.hsgamer.hscore.addon.exception;

/**
 * A runtime exception for required addon path
 */
public final class RequiredAddonPathException extends RuntimeException {

  public RequiredAddonPathException(final String message) {
    super(message);
  }
}
