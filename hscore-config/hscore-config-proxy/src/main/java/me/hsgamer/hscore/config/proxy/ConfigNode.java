package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotation.converter.Converter;

public class ConfigNode {
  private final String path;
  private final Config config;
  private final Converter converter;
  private final Object defaultValue;

  private ConfigNode(String path, Config config, Converter converter, Object defaultValue) {
    this.path = path;
    this.config = config;
    this.converter = converter;
    this.defaultValue = defaultValue;
    config.addDefault(path, converter.convertToRaw(defaultValue));
  }

  ConfigNode(String path, Config config, Class<? extends Converter> converter, Object defaultValue) {
    this(path, config, Converter.createConverterSafe(converter), defaultValue);
  }

  public String getPath() {
    return path;
  }

  public Object getValue() {
    Object rawValue = config.getNormalized(path);
    if (rawValue == null) {
      return defaultValue;
    }
    Object value = converter.convert(rawValue);
    return value == null ? defaultValue : value;
  }

  public void setValue(Object value) {
    config.set(path, converter.convertToRaw(value));
  }
}
