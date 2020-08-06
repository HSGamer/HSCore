package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.ConfigPath;

public class SimpleConfigPath<T> extends ConfigPath<T> {

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
