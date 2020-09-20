package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.Color;

public final class ColorConfigPath extends SerializableMapConfigPath<Color> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public ColorConfigPath(final String path, final Color def) {
    super(path, def);
  }

  @Override
  public Color convert(final Map<String, Object> rawValue) {
    return Color.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final Color value) {
    return value.serialize();
  }

}
