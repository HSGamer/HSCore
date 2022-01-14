package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.path.BaseConfigPath;

public class SimpleConfigPath<T> extends BaseConfigPath<T> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  @SuppressWarnings("unchecked")
  public SimpleConfigPath(String path, T def) {
    super(path, def, o -> (T) o);
  }
}
