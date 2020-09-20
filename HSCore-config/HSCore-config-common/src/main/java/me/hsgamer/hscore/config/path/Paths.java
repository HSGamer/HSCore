package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

/**
 * Utility to easily create {@link DefaultPath}
 */
public final class Paths {

  private Paths() {
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static BooleanDefaultPath booleanPath(final String path, final boolean def) {
    return new BooleanDefaultPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static DoubleDefaultPath doublePath(final String path, final Double def) {
    return new DoubleDefaultPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static FloatDefaultPath floatPath(final String path, final Float def) {
    return new FloatDefaultPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static IntegerDefaultPath integerPath(final String path, final Integer def) {
    return new IntegerDefaultPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static LongDefaultPath longPath(final String path, final Long def) {
    return new LongDefaultPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @param <T> the type of the value
   * @return the config path
   */
  public static <T> SimpleDefaultPath<T> simplePath(final String path, final T def) {
    return new SimpleDefaultPath<>(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static StringDefaultPath stringPath(final String path, final String def) {
    return new StringDefaultPath(path, def);
  }

}
