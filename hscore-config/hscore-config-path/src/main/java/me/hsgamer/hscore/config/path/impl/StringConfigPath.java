package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.BaseConfigPath;

public class StringConfigPath extends BaseConfigPath<String> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public StringConfigPath(PathString path, String def) {
    super(path, def, String::valueOf);
  }
}
