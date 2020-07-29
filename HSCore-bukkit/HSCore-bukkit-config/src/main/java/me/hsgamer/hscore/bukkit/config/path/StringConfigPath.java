package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.ConfigPath;

public class StringConfigPath extends ConfigPath<String> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public StringConfigPath(String path, String def) {
    super(path, def, String::valueOf);
  }
}
