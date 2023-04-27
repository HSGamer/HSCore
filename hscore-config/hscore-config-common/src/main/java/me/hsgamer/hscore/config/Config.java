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
   * @param path the path
   * @param def  the default value if the value is not found
   *
   * @return the value
   */
  Object get(PathString path, Object def);

  /**
   * Set the value to the path
   *
   * @param path  the path
   * @param value the value
   */
  void set(PathString path, Object value);

  /**
   * Check if the configuration contains the path
   *
   * @param path the path
   *
   * @return true if it does
   */
  default boolean contains(PathString path) {
    return get(path, null) != null;
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
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  Map<PathString, Object> getValues(PathString path, boolean deep);

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
  default void remove(PathString path) {
    if (path.isRoot()) {
      clear();
    } else {
      set(path, null);
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
  default Object get(PathString path) {
    return get(path, null);
  }

  /**
   * Get the normalized value from the path
   *
   * @param path the path
   * @param def  the default value the default value if the value is not found
   *
   * @return the value
   */
  default Object getNormalized(PathString path, Object def) {
    return normalizeObject(get(path, def));
  }

  /**
   * Get the normalized value from the path
   *
   * @param path the path
   *
   * @return the value
   */
  default Object getNormalized(PathString path) {
    return getNormalized(path, null);
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
  default <T> T getInstance(PathString path, T def, Class<T> type) {
    Object value = getNormalized(path, def);
    if (type == String.class) {
      // noinspection unchecked
      return value != null ? (T) String.valueOf(value) : def;
    }
    return type.isInstance(value) ? type.cast(value) : def;
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
  default <T> T getInstance(PathString path, Class<T> type) {
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
  default boolean isInstance(PathString path, Class<?> type) {
    return type.isInstance(get(path));
  }

  /**
   * Get all values from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<PathString, Object> getValues(boolean deep) {
    return getValues(PathString.ROOT, deep);
  }

  /**
   * Get all keys from the path
   *
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the keys
   */
  default Set<PathString> getKeys(PathString path, boolean deep) {
    return getValues(path, deep).keySet();
  }

  /**
   * Get all keys from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the keys
   */
  default Set<PathString> getKeys(boolean deep) {
    return getKeys(PathString.ROOT, deep);
  }

  /**
   * Get all normalized values from the path
   *
   * @param path the path
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<PathString, Object> getNormalizedValues(PathString path, boolean deep) {
    Map<PathString, Object> normalized = new LinkedHashMap<>();
    getValues(path, deep).forEach((k, v) -> normalized.put(k, normalizeObject(v)));
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
   * Get all values from the root path
   *
   * @param deep should we go deeper from the path?
   *
   * @return the values
   */
  default Map<PathString, Object> getNormalizedValues(boolean deep) {
    return getNormalizedValues(PathString.ROOT, deep);
  }

  /**
   * Add a default value to the path
   *
   * @param path  the path
   * @param value the value
   */
  default void addDefault(PathString path, Object value) {
    if (!contains(path)) {
      set(path, value);
    }
  }

  /**
   * Add default values
   *
   * @param map the map of default values
   */
  default void addDefaults(Map<PathString, Object> map) {
    map.forEach(this::addDefault);
  }

  /**
   * Get the comment.
   * This is a default empty method. The implementation can override this method to support comments.
   *
   * @param path the path
   * @param type the comment type
   *
   * @return the comment
   */
  default List<String> getComment(PathString path, CommentType type) {
    return Collections.emptyList();
  }

  /**
   * Set the comment
   * This is a default empty method. The implementation can override this method to support comments.
   *
   * @param path  the path
   * @param value the comment
   * @param type  the comment type
   */
  default void setComment(PathString path, List<String> value, CommentType type) {
    // EMPTY
  }

  /**
   * Get the block comment
   *
   * @param path the path
   *
   * @return the comment
   *
   * @see #getComment(PathString, CommentType)
   */
  default List<String> getComment(PathString path) {
    return getComment(path, CommentType.BLOCK);
  }

  /**
   * Set the block comment
   *
   * @param path  the path
   * @param value the comment
   *
   * @see #setComment(PathString, List, CommentType)
   */
  default void setComment(PathString path, List<String> value) {
    setComment(path, value, CommentType.BLOCK);
  }
}
