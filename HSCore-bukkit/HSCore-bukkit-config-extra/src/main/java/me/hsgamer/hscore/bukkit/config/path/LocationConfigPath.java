package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.Location;

public final class LocationConfigPath extends SerializableMapConfigPath<Location> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public LocationConfigPath(final String path, final Location def) {
    super(path, def);
  }

  @Override
  public Location convert(final Map<String, Object> rawValue) {
    return Location.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final Location value) {
    return value.serialize();
  }

}
