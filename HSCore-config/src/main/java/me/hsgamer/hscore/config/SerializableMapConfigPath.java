package me.hsgamer.hscore.config;

import java.util.Map;

public abstract class SerializableMapConfigPath<T> extends AdvancedConfigPath<Map<String, Object>, T> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public SerializableMapConfigPath(String path, T def) {
    super(path, def);
  }

  @Override
  public Map<String, Object> getFromConfig(Config config) {
    return config.getConfig().getConfigurationSection(path).getValues(false);
  }

}
