package me.hsgamer.hscore.expansion.common.exception;

import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;

/**
 * The exception thrown when the {@link ExpansionClassLoader} throws an exception
 */
public class ExpansionClassLoaderException extends RuntimeException {
  private final ExpansionClassLoader expansionClassLoader;

  public ExpansionClassLoaderException(ExpansionClassLoader expansionClassLoader, String message, Throwable cause) {
    super(message, cause);
    this.expansionClassLoader = expansionClassLoader;
  }

  /**
   * Get the {@link ExpansionClassLoader}
   *
   * @return the {@link ExpansionClassLoader}
   */
  public ExpansionClassLoader getExpansionClassLoader() {
    return expansionClassLoader;
  }
}
