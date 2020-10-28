package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.BaseConfigPath;
import me.hsgamer.hscore.config.CommentablePath;
import me.hsgamer.hscore.config.ConfigPath;
import org.jetbrains.annotations.NotNull;

/**
 * Utility to easily create {@link BaseConfigPath}
 */
public final class Paths {

  private Paths() {
  }

  /**
   * Create a commentable path
   *
   * @param path     the path to the value
   * @param comments the comment to create
   * @param <T>      path's value type
   * @return the commentable path
   */
  public static <T> CommentablePath<T> commented(@NotNull ConfigPath<T> path, @NotNull String... comments) {
    return new CommentablePath<>(path, comments);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static BooleanConfigPath booleanPath(String path, boolean def) {
    return new BooleanConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static DoubleConfigPath doublePath(String path, Double def) {
    return new DoubleConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static FloatConfigPath floatPath(String path, Float def) {
    return new FloatConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static IntegerConfigPath integerPath(String path, Integer def) {
    return new IntegerConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static LongConfigPath longPath(String path, Long def) {
    return new LongConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @param <T>  the type of the value
   * @return the config path
   */
  public static <T> SimpleConfigPath<T> simplePath(String path, T def) {
    return new SimpleConfigPath<>(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the config path
   */
  public static StringConfigPath stringPath(String path, String def) {
    return new StringConfigPath(path, def);
  }
}
