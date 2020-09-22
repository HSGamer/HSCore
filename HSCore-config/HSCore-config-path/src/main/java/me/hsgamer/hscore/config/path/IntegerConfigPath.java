package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.BaseConfigPath;

public final class IntegerConfigPath extends BaseConfigPath<Integer> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public IntegerConfigPath(String path, Integer def) {
    super(path, def, o -> {
      try {
        return Integer.parseInt(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
