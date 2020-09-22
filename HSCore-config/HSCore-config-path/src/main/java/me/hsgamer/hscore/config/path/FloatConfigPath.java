package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.ConfigPath;

public final class FloatConfigPath extends ConfigPath<Float> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public FloatConfigPath(String path, Float def) {
    super(path, def, o -> {
      try {
        return Float.parseFloat(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
