package me.hsgamer.hscore.downloader.exception;

import org.jetbrains.annotations.NotNull;

/**
 * A runtime exception for required info key
 */
public final class RequiredInfoKeyException extends RuntimeException {

  /**
   * Create an exception to throw when a download info has missing keys
   *
   * @param message the message to print
   */
  public RequiredInfoKeyException(@NotNull final String message) {
    super(message);
  }
}
