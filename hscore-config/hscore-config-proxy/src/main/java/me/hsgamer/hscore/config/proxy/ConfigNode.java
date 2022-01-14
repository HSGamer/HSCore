package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotation.converter.Converter;

/**
 * The config node for a method in the interface
 */
public class ConfigNode {
  private final String path;
  private final Config config;
  private final Converter converter;
  private final Object defaultValue;

  /**
   * Constructor
   *
   * @param path         the path
   * @param config       the config
   * @param converter    the converter
   * @param defaultValue the default value
   */
  private ConfigNode(String path, Config config, Converter converter, Object defaultValue) {
    this.path = path;
    this.config = config;
    this.converter = converter;
    this.defaultValue = defaultValue;
    config.addDefault(path, converter.convertToRaw(defaultValue));
  }

  /**
   * Constructor
   *
   * @param path         the path
   * @param config       the config
   * @param converter    the converter
   * @param defaultValue the default value
   */
  ConfigNode(String path, Config config, Class<? extends Converter> converter, Object defaultValue) {
    this(path, config, Converter.createConverterSafe(converter), defaultValue);
  }

  /**
   * Get the config path
   *
   * @return the config path
   */
  public String getPath() {
    return path;
  }

  /**
   * Get the value from the config
   *
   * @return the value
   */
  public Object getValue() {
    Object rawValue = config.getNormalized(path);
    if (rawValue == null) {
      return defaultValue;
    }
    Object value = converter.convert(rawValue);
    return value == null ? defaultValue : value;
  }

  /**
   * Set the value to the config
   *
   * @param value the value
   */
  public void setValue(Object value) {
    config.set(path, converter.convertToRaw(value));
  }
}
