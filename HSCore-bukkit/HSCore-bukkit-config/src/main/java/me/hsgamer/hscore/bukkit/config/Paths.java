package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.bukkit.config.path.BooleanConfigPath;
import me.hsgamer.hscore.bukkit.config.path.DoubleConfigPath;
import me.hsgamer.hscore.bukkit.config.path.FloatConfigPath;
import me.hsgamer.hscore.bukkit.config.path.IntegerConfigPath;
import me.hsgamer.hscore.bukkit.config.path.LongConfigPath;
import me.hsgamer.hscore.bukkit.config.path.SimpleConfigPath;
import me.hsgamer.hscore.bukkit.config.path.StringConfigPath;

/**
 * Utility to easily create {@link ConfigPath}
 */
public final class Paths {

  private Paths() {
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static BooleanConfigPath booleanPath(String path, boolean def) {
    return new BooleanConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static DoubleConfigPath doublePath(String path, Double def) {
    return new DoubleConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static FloatConfigPath floatPath(String path, Float def) {
    return new FloatConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static IntegerConfigPath integerPath(String path, Integer def) {
    return new IntegerConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static LongConfigPath longPath(String path, Long def) {
    return new LongConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static <T> SimpleConfigPath<T> simplePath(String path, T def) {
    return new SimpleConfigPath<>(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public static StringConfigPath stringPath(String path, String def) {
    return new StringConfigPath(path, def);
  }
}
