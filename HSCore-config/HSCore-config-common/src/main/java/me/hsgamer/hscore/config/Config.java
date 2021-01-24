package me.hsgamer.hscore.config;

import java.util.*;
import java.util.logging.Logger;

/**
 * The interface for all configurations
 */
public interface Config {
  /**
   * The logger for ease
   */
  Logger LOGGER = Logger.getLogger("Config");

  /**
   * Get the original instance
   *
   * @return the original instance
   */
  Object getOriginal();

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
   * Setup the configuration
   */
  void setup();

  /**
   * Save the configuration
   */
  void save();

  /**
   * Reload the configuration
   */
  void reload();

  /**
   * Normalize the library-specific object
   *
   * @param object the object
   *
   * @return the normalized object
   */
  Object normalize(Object object);

  /**
   * Check if the object is normalizable
   *
   * @param object the object
   *
   * @return true if it is
   */
  boolean isNormalizable(Object object);

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
   * Get the normalized value from the path
   *
   * @param path the path
   *
   * @return the value
   */
  default Object getNormalized(String path) {
    return normalize(get(path));
  }

  /**
   * Get the normalized value from the path
   *
   * @param path the path
   * @param def  the default value the default value if the value is not found
   *
   * @return the value
   */
  default Object getNormalized(String path, Object def) {
    return normalize(get(path, def));
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
    Object value = getNormalized(path, def);
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
   * Get all keys from the path
   *
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the keys
   */
  default Set<String> getKeys(String path, boolean deep) {
    return getValues(path, deep).keySet();
  }

  /**
   * Get all keys from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the keys
   */
  default Set<String> getKeys(boolean deep) {
    return getKeys("", deep);
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
    getValues(path, deep).forEach((k, v) -> {
      if (!isNormalizable(v)) {
        normalized.put(k, v);
      }
      Object normalizedValue = normalize(v);
      if (normalizedValue instanceof Map) {
        // noinspection unchecked
        ((Map) normalizedValue).replaceAll((k1, v1) -> isNormalizable(v1) ? normalize(v1) : v1);
      } else if (normalizedValue instanceof Collection) {
        List<Object> normalizedList = new ArrayList<>();
        // noinspection unchecked
        ((Collection) normalizedValue).forEach(v1 -> normalizedList.add(isNormalizable(v1) ? normalize(v1) : v1));
        normalizedValue = normalizedList;
      }
      normalized.put(k, normalizedValue);
    });
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
   * Add default values
   *
   * @param map the map of default values
   */
  default void addDefaults(Map<String, Object> map) {
    map.forEach(this::addDefault);
  }
}
