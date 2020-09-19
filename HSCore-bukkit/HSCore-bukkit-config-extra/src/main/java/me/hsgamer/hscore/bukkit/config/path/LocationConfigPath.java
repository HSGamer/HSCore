package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.AdvancedConfigPath;
import me.hsgamer.hscore.config.Config;
import org.bukkit.Location;

import java.util.Map;

public class LocationConfigPath extends AdvancedConfigPath<Map<String, Object>, Location> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public LocationConfigPath(String path, Location def) {
    super(path, def);
  }

  @Override
  public Map<String, Object> getFromConfig(Config config) {
    return config.getConfig().getConfigurationSection(path).getValues(false);
  }

  @Override
  public Location convert(Map<String, Object> rawValue) {
    return Location.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(Location value) {
    return value.serialize();
  }
}
