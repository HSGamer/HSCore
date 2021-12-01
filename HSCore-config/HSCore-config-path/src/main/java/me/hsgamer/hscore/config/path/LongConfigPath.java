package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.BaseConfigPath;

public class LongConfigPath extends BaseConfigPath<Long> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public LongConfigPath(String path, Long def) {
    super(path, def, o -> {
      try {
        return Long.parseLong(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
