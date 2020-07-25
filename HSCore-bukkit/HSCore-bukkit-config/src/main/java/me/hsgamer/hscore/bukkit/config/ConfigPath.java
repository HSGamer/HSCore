package me.hsgamer.hscore.bukkit.config;

/**
 * A simple config path for PluginConfig
 *
 * @param <T> the type of the value
 */
public class ConfigPath<T> {

  private final Class<T> clazz;
  private final String path;
  private final T def;
  private PluginConfig config;

  /**
   * Create a config path
   *
   * @param clazz the type of value
   * @param path  the path to the value
   * @param def   the default value if it's not found
   */
  public ConfigPath(Class<T> clazz, String path, T def) {
    this.clazz = clazz;
    this.path = path;
    this.def = def;
  }

  /**
   * Set the config for this ConfigPath
   *
   * @param config the config
   */
  public void setConfig(PluginConfig config) {
    this.config = config;
    config.getConfig().addDefault(path, def);
  }

  /**
   * Get the value
   *
   * @return the value
   */
  public T getValue() {
    if (config == null) {
      return def;
    }
    return config.get(clazz, path, def);
  }

  /**
   * Set the value
   *
   * @param value the value
   */
  public void setValue(T value) {
    if (config == null) {
      return;
    }

    config.getConfig().set(path, value);
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }
}
