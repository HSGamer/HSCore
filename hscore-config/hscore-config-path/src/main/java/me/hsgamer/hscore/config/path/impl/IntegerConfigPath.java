package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.BaseConfigPath;

public class IntegerConfigPath extends BaseConfigPath<Integer> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public IntegerConfigPath(PathString path, Integer def) {
    super(path, def, o -> {
      try {
        return Integer.parseInt(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
