package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.bukkit.config.path.*;

public final class Paths {

  private Paths() {
  }

  public static BooleanConfigPath booleanPath(String path, boolean def) {
    return new BooleanConfigPath(path, def);
  }

  public static DoubleConfigPath doublePath(String path, Double def) {
    return new DoubleConfigPath(path, def);
  }

  public static FloatConfigPath floatPath(String path, Float def) {
    return new FloatConfigPath(path, def);
  }

  public static IntegerConfigPath integerPath(String path, Integer def) {
    return new IntegerConfigPath(path, def);
  }

  public static LongConfigPath longPath(String path, Long def) {
    return new LongConfigPath(path, def);
  }

  public static <T> SimpleConfigPath<T> simplePath(String path, T def) {
    return new SimpleConfigPath<>(path, def);
  }

  public static StringConfigPath stringPath(String path, String def) {
    return new StringConfigPath(path, def);
  }
}
