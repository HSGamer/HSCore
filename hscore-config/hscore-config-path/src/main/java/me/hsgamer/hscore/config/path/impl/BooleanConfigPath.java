package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.BaseConfigPath;

public class BooleanConfigPath extends BaseConfigPath<Boolean> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public BooleanConfigPath(PathString path, Boolean def) {
    super(path, def, o -> Boolean.parseBoolean(String.valueOf(o)));
  }
}
