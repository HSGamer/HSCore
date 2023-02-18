package me.hsgamer.hscore.expansion.common.exception;

import me.hsgamer.hscore.expansion.common.Expansion;

import java.io.File;

/**
 * The exception thrown when the file is not a valid {@link Expansion} file
 */
public class InvalidExpansionFileException extends RuntimeException {
  private final File file;

  public InvalidExpansionFileException(String message, File file, Throwable cause) {
    super(message, cause);
    this.file = file;
  }

  /**
   * Get the file
   *
   * @return the file
   */
  public File getFile() {
    return file;
  }
}
