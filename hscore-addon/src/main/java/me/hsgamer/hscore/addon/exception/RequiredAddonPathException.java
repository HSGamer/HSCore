package me.hsgamer.hscore.addon.exception;

import org.jetbrains.annotations.NotNull;

/**
 * A runtime exception for required addon path
 */
public final class RequiredAddonPathException extends RuntimeException {

  /**
   * Create an exception to throw when an addon's configuration file has missing paths
   *
   * @param message the message to print
   */
  public RequiredAddonPathException(@NotNull final String message) {
    super(message);
  }
}
