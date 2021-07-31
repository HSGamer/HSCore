package me.hsgamer.hscore.config;

/**
 * The options for {@link Config}
 */
public class ConfigOptions {
  private char pathSeparator = '.';
  private int indent = 2;

  /**
   * Get the path separator
   *
   * @return the path separator
   */
  public char getPathSeparator() {
    return pathSeparator;
  }

  /**
   * Set the path separator
   *
   * @param pathSeparator the path separator
   */
  public void setPathSeparator(char pathSeparator) {
    this.pathSeparator = pathSeparator;
  }

  /**
   * Get the indent of the config
   *
   * @return the indent
   */
  public int getIndent() {
    return indent;
  }

  /**
   * Set the indent of the config
   *
   * @param indent the indent
   */
  public void setIndent(int indent) {
    if (indent > 0) {
      this.indent = indent;
    }
  }
}
