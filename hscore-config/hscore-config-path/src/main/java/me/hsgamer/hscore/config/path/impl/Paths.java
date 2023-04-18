package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.CommentablePath;
import me.hsgamer.hscore.config.path.ConfigPath;
import me.hsgamer.hscore.config.path.StickyConfigPath;
import org.jetbrains.annotations.NotNull;

/**
 * Utility to easily create {@link ConfigPath}
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
   *
   * @return the commentable path
   */
  public static <T> CommentablePath<T> commented(@NotNull ConfigPath<T> path, @NotNull String... comments) {
    return new CommentablePath<>(path, comments);
  }

  /**
   * Create a sticky config path
   *
   * @param path the path to the value
   * @param <T>  the type of the value
   *
   * @return the sticky config path
   */
  public static <T> StickyConfigPath<T> sticky(@NotNull ConfigPath<T> path) {
    return new StickyConfigPath<>(path);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static BooleanConfigPath booleanPath(PathString path, boolean def) {
    return new BooleanConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static DoubleConfigPath doublePath(PathString path, Double def) {
    return new DoubleConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static FloatConfigPath floatPath(PathString path, Float def) {
    return new FloatConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static IntegerConfigPath integerPath(PathString path, Integer def) {
    return new IntegerConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static LongConfigPath longPath(PathString path, Long def) {
    return new LongConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @param <T>  the type of the value
   *
   * @return the config path
   */
  public static <T> SimpleConfigPath<T> simplePath(PathString path, T def) {
    return new SimpleConfigPath<>(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static StringConfigPath stringPath(PathString path, String def) {
    return new StringConfigPath(path, def);
  }
}
