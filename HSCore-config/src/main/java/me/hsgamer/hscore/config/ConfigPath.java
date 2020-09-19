package me.hsgamer.hscore.config;

import java.util.function.Function;

/**
 * A simple config path for PluginConfig
 *
 * @param <T> the type of the value
 */
public abstract class ConfigPath<T> extends BaseConfigPath<T> {

  private final Function<Object, T> typeConverter;

  /**
   * Create a config path
   *
   * @param path          the path to the value
   * @param def           the default value if it's not found
   * @param typeConverter how to convert the raw object to the needed type of value
   */
  public ConfigPath(String path, T def, Function<Object, T> typeConverter) {
    super(path, def);
    this.typeConverter = typeConverter;
  }

  @Override
  public T getValue() {
    if (config == null) {
      return def;
    }

    Object rawValue = config.get(path, def);
    if (rawValue == null) {
      return def;
    }

    return typeConverter.apply(rawValue);
  }

  @Override
  public void setConfig(Config config) {
    super.setConfig(config);
    config.getConfig().addDefault(path, def);
  }
}
