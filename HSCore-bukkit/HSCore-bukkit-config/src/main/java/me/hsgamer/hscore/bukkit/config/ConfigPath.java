package me.hsgamer.hscore.bukkit.config;

import java.util.function.Function;

/**
 * A simple config path for PluginConfig
 *
 * @param <T> the type of the value
 */
public class ConfigPath<T> {

  protected final T def;
  private final String path;
  private final Function<Object, T> typeConverter;
  private PluginConfig config;

  /**
   * Create a config path
   *
   * @param path          the path to the value
   * @param def           the default value if it's not found
   * @param typeConverter how to convert the raw object to the needed type of value
   */
  public ConfigPath(String path, T def, Function<Object, T> typeConverter) {
    this.path = path;
    this.def = def;
    this.typeConverter = typeConverter;
  }

  /**
   * Set the config for this ConfigPath.
   * <br>This only adds the default value to the config.
   * <br>You will need to enable the copyDefault option and save the config manually.
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

    return typeConverter.apply(config.get(path, def));
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
