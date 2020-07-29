package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.ConfigPath;

public class FloatConfigPath extends ConfigPath<Float> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public FloatConfigPath(String path, Float def) {
    super(path, def, o -> Float.parseFloat(String.valueOf(o)));
  }
}
