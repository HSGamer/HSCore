package me.hsgamer.hscore.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The interface for all configurations
 */
public interface Configuration {

  /**
   * Get the value from the path
   *
   * @param path the path
   * @param def  the default value if the value is not found
   *
   * @return the value
   */
  Object get(String path, Object def);

  /**
   * Set the value to the path
   *
   * @param path  the path
   * @param value the value
   */
  void set(String path, Object value);

  /**
   * Check if the configuration contains the path
   *
   * @param path the path
   *
   * @return true if it does
   */
  boolean contains(String path);

  /**
   * Get the name of the configuration
   *
   * @return the name
   */
  String getName();

  /**
   * Add a default value to the path
   *
   * @param path  the path
   * @param value the value
   */
  void addDefault(String path, Object value);

  /**
   * Get all values from the path
   *
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  Map<String, Object> getValues(String path, boolean deep);

  /**
   * Get the value from the path
   *
   * @param path the path
   *
   * @return the value
   */
  default Object get(String path) {
    return get(path, null);
  }

  /**
   * Get the value from the path
   *
   * @param path the path
   * @param def  the default value if the value is not found
   * @param type the type class of the value
   * @param <T>  the type of the value
   *
   * @return the value
   */
  default <T> T getInstance(String path, T def, Class<T> type) {
    Object value = get(path, def);
    // noinspection unchecked
    return type.isInstance(type) ? (T) value : def;
  }

  /**
   * Get the value from the path
   *
   * @param path the path
   * @param type the type class of the value
   * @param <T>  the type of the value
   *
   * @return the value
   */
  default <T> T getInstance(String path, Class<T> type) {
    return getInstance(path, null, type);
  }

  /**
   * Check if the value of the path matches the type
   *
   * @param path the path
   * @param type the type class of the value
   *
   * @return true if it does
   */
  default boolean isInstance(String path, Class<?> type) {
    return type.isInstance(get(path));
  }

  /**
   * Get all values from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<String, Object> getValues(boolean deep) {
    return getValues("", deep);
  }

  /**
   * Get all normalized values from the path
   *
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<String, Object> getNormalizedValues(String path, boolean deep) {
    Map<String, Object> normalized = new LinkedHashMap<>();
    getValues(path, deep).forEach((k, v) -> normalized.put(k, normalize(v)));
    return normalized;
  }

  /**
   * Get all values from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<String, Object> getNormalizedValues(boolean deep) {
    return getNormalizedValues("", deep);
  }

  /**
   * Normalize the library-specific object
   *
   * @param object the object
   *
   * @return the normalized object
   */
  default Object normalize(Object object) {
    return object;
  }

  /**
   * Add default values
   *
   * @param map the map of default values
   */
  default void addDefaults(Map<String, Object> map) {
    map.forEach(this::addDefault);
  }
}
