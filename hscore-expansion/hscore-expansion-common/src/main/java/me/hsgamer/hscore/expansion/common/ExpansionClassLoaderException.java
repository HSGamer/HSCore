package me.hsgamer.hscore.expansion.common;

/**
 * The exception thrown when the {@link ExpansionClassLoader} throws an exception
 */
public class ExpansionClassLoaderException extends RuntimeException {
  private final ExpansionClassLoader expansionClassLoader;

  ExpansionClassLoaderException(ExpansionClassLoader expansionClassLoader, String message, Throwable cause) {
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
