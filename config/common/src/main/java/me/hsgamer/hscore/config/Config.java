package me.hsgamer.hscore.config;

import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

import java.util.*;

/**
 * The interface for all configurations
 */
public interface Config {
  /**
   * The logger for ease
   */
  Logger LOGGER = LoggerProvider.getLogger(Config.class);

  /**
   * Get the original instance
   *
   * @return the original instance
   */
  Object getOriginal();

  /**
   * Get the value from the path
   *
   * @param def  the default value if the value is not found
   * @param path the path
   *
   * @return the value
   */
  Object get(Object def, String... path);

  /**
   * Set the value to the path
   *
   * @param value the value
   * @param path  the path
   */
  void set(Object value, String... path);

  /**
   * Check if the configuration contains the path
   *
   * @param path the path
   *
   * @return true if it does
   */
  default boolean contains(String... path) {
    return get(null, path) != null;
  }

  /**
   * Get the name of the configuration
   *
   * @return the name
   */
  String getName();

  /**
   * Get all values from the path
   *
   * @param deep should we go deeper from the path?
   * @param path the path
   *
   * @return the values
   */
  Map<String[], Object> getValues(boolean deep, String... path);

  /**
   * Set up the configuration
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
   * Remove the path from the configuration
   *
   * @param path the path
   */
  default void remove(String... path) {
    if (path.length == 0) {
      clear();
    } else {
      set(null, path);
    }
  }

  /**
   * Remove all paths from the configuration
   */
  default void clear() {
    getKeys(false).forEach(this::remove);
  }

  /**
   * Get the value from the path
   *
   * @param path the path
   *
   * @return the value
   */
  default Object get(String... path) {
    return get(null, path);
  }

  /**
   * Get the normalized value from the path
   *
   * @param def  the default value the default value if the value is not found
   * @param path the path
   *
   * @return the value
   */
  default Object getNormalized(Object def, String... path) {
    return normalizeObject(get(def, path));
  }

  /**
   * Get the normalized value from the path
   *
   * @param path the path
   *
   * @return the value
   */
  default Object getNormalized(String... path) {
    return getNormalized(null, path);
  }

  /**
   * Get the value from the path
   *
   * @param type the type class of the value
   * @param def  the default value if the value is not found
   * @param path the path
   * @param <T>  the type of the value
   *
   * @return the value
   */
  default <T> T getInstance(Class<T> type, T def, String... path) {
    Object value = getNormalized(def, path);
    if (type == String.class) {
      // noinspection unchecked
      return value != null ? (T) String.valueOf(value) : def;
    }
    return type.isInstance(value) ? type.cast(value) : def;
  }

  /**
   * Get the value from the path
   *
   * @param type the type class of the value
   * @param path the path
   * @param <T>  the type of the value
   *
   * @return the value
   */
  default <T> T getInstance(Class<T> type, String... path) {
    return getInstance(type, null, path);
  }

  /**
   * Check if the value of the path matches the type
   *
   * @param type the type class of the value
   * @param path the path
   *
   * @return true if it does
   */
  default boolean isInstance(Class<?> type, String... path) {
    return type.isInstance(get(path));
  }

  /**
   * Get all keys from the path
   *
   * @param deep should we go deeper from the path?
   * @param path the path
   *
   * @return the keys
   */
  default Set<String[]> getKeys(boolean deep, String... path) {
    return getValues(deep, path).keySet();
  }

  /**
   * Get all normalized values from the path
   *
   * @param deep should we go deeper from the path?
   * @param path the path
   *
   * @return the values
   */
  default Map<String[], Object> getNormalizedValues(boolean deep, String... path) {
    Map<String[], Object> normalized = new LinkedHashMap<>();
    getValues(deep, path).forEach((k, v) -> normalized.put(k, normalizeObject(v)));
    return normalized;
  }

  /**
   * Normalize the object and its elements if it is a map or a collection
   *
   * @param object the object
   *
   * @return the normalized object
   */
  default Object normalizeObject(Object object) {
    Object normalizedValue = isNormalizable(object) ? normalize(object) : object;
    if (normalizedValue instanceof Map) {
      Map<Object, Object> normalizedMap = new LinkedHashMap<>();
      ((Map<?, ?>) normalizedValue).forEach((k, v) -> normalizedMap.put(k, normalizeObject(v)));
      normalizedValue = normalizedMap;
    } else if (normalizedValue instanceof Collection) {
      List<Object> normalizedList = new ArrayList<>();
      ((Collection<?>) normalizedValue).forEach(v -> normalizedList.add(normalizeObject(v)));
      normalizedValue = normalizedList;
    }
    return normalizedValue;
  }

  /**
   * Set the value to the path if it is not already set
   *
   * @param value the value
   * @param path  the path
   */
  default void setIfAbsent(Object value, String... path) {
    if (!contains(path)) {
      set(value, path);
    }
  }

  /**
   * Set the values to the path if they are not already set
   *
   * @param map the map of values
   */
  default void setIfAbsent(Map<String[], Object> map) {
    map.forEach((k, v) -> setIfAbsent(v, k));
  }

  /**
   * Get the comment.
   * This is a default empty method. The implementation can override this method to support comments.
   *
   * @param type the comment type
   * @param path the path
   *
   * @return the comment
   */
  default List<String> getComment(CommentType type, String... path) {
    return Collections.emptyList();
  }

  /**
   * Set the comment
   * This is a default empty method. The implementation can override this method to support comments.
   *
   * @param type  the comment type
   * @param value the comment, can be null to remove the comment
   * @param path  the path
   */
  default void setComment(CommentType type, List<String> value, String... path) {
    // EMPTY
  }

  /**
   * Get the block comment
   *
   * @param path the path
   *
   * @return the comment
   *
   * @see #getComment(CommentType, String[])
   */
  default List<String> getComment(String... path) {
    return getComment(CommentType.BLOCK, path);
  }

  /**
   * Set the block comment
   *
   * @param path  the path
   * @param value the comment, can be null to remove the comment
   *
   * @see #setComment(CommentType, List, String[])
   */
  default void setComment(List<String> value, String... path) {
    setComment(CommentType.BLOCK, value, path);
  }
}
