package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.ConfigPath;

public class LongConfigPath extends ConfigPath<Long> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public LongConfigPath(String path, Long def) {
    super(path, def, o -> Long.parseLong(String.valueOf(o)));
  }
}
