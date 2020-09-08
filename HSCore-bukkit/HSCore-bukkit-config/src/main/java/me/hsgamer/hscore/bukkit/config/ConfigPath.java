package me.hsgamer.hscore.bukkit.config;

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

    return typeConverter.apply(config.get(path, def));
  }
}
