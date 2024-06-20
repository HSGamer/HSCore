package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotation.converter.Converter;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The config node for a method in the interface
 */
public class ConfigNode {
  private final PathString path;
  private final Config config;
  private final Converter converter;
  private final Object defaultValue;
  private final List<String> comment;
  private final boolean stickyValue;
  private final AtomicReference<Object> cachedValue = new AtomicReference<>();

  /**
   * Constructor
   *
   * @param path         the path
   * @param config       the config
   * @param converter    the converter
   * @param defaultValue the default value
   * @param comment      the comment
   * @param stickyValue  true if the value should be sticky
   */
  ConfigNode(String[] path, Config config, Converter converter, Object defaultValue, List<String> comment, boolean stickyValue) {
    this.path = new PathString(path);
    this.config = config;
    this.converter = converter;
    this.defaultValue = defaultValue;
    this.comment = comment;
    this.stickyValue = stickyValue;
  }

  /**
   * Get the config path
   *
   * @return the config path
   */
  public PathString getPath() {
    return path;
  }

  /**
   * Add the default value to the config
   */
  public void addDefault() {
    config.setIfAbsent(path, converter.convertToRaw(defaultValue));
    if (!comment.isEmpty() && config.getComment(path).isEmpty()) {
      config.setComment(path, comment);
    }
  }

  /**
   * Get the value from the config
   *
   * @return the value
   */
  public Object getValue() {
    Object cached = cachedValue.get();
    if (cached != null && stickyValue) {
      return cached;
    }

    Object rawValue = config.getNormalized(path);
    if (rawValue == null) {
      return defaultValue;
    }
    Object value = converter.convert(rawValue);
    Object finalValue = value == null ? defaultValue : value;
    cachedValue.set(finalValue);
    return finalValue;
  }

  /**
   * Set the value to the config
   *
   * @param value the value
   */
  public void setValue(Object value) {
    config.set(path, converter.convertToRaw(value));
    this.cachedValue.set(null);
  }

  /**
   * Clear the cached value
   */
  public void clearCache() {
    cachedValue.set(null);
  }
}
