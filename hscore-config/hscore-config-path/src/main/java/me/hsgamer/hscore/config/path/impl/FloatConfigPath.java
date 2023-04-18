package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.BaseConfigPath;

public class FloatConfigPath extends BaseConfigPath<Float> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public FloatConfigPath(PathString path, Float def) {
    super(path, def, o -> {
      try {
        return Float.parseFloat(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
