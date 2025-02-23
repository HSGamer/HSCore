package me.hsgamer.hscore.common;

import java.util.*;
import java.util.function.Supplier;

/**
 * Methods on Map
 */
public class MapUtils {
  private MapUtils() {
    // EMPTY
  }

  /**
   * Get the value given the key from the map
   *
   * @param map          the map
   * @param defaultValue the default value
   * @param key          the key
   * @param <K>          the key type
   * @param <V>          the value type
   *
   * @return the value
   */
  @SafeVarargs
  public static <K, V> V getIfFoundOrDefault(Map<K, V> map, V defaultValue, K... key) {
    for (K k : key) {
      if (map.containsKey(k)) {
        return map.get(k);
      }
    }
    return defaultValue;
  }

  /**
   * Get the value given the key from the map
   *
   * @param map the map
   * @param key the key
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the value, or null if not found
   */
  @SafeVarargs
  public static <K, V> V getIfFound(Map<K, V> map, K... key) {
    return getIfFoundOrDefault(map, null, key);
  }

  /**
   * Get the value given the key from the map
   *
   * @param map the map
   * @param key the key
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the value, or empty optional if not found
   */
  @SafeVarargs
  public static <K, V> Optional<V> getOptional(Map<K, V> map, K... key) {
    return Optional.ofNullable(getIfFound(map, key));
  }

  /**
   * Check if the map contains any of the keys
   *
   * @param map the map
   * @param key the key
   * @param <K> the key type
   *
   * @return true if it does
   */
  @SafeVarargs
  public static <K> boolean containsAnyKey(Map<K, ?> map, K... key) {
    for (K k : key) {
      if (map.containsKey(k)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Cast to the optional string-object map
   *
   * @param object the object
   * @param unsafe whether to cast unsafe. Set to false if you want to ensure the map is string-object map, but it will be slower.
   *
   * @return the map, or empty optional if it's not a map
   */
  public static Optional<Map<String, Object>> castOptionalStringObjectMap(Object object, boolean unsafe) {
    if (object instanceof Map) {
      if (unsafe) {
        try {
          // noinspection unchecked
          return Optional.of((Map<String, Object>) object);
        } catch (Throwable ignored) {
          // IGNORED
        }
      } else {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
          map.put(Objects.toString(entry.getKey()), entry.getValue());
        }
        return Optional.of(map);
      }
    }
    return Optional.empty();
  }

  /**
   * Cast to the optional string-object map
   *
   * @param object the object
   *
   * @return the map
   */
  public static Optional<Map<String, Object>> castOptionalStringObjectMap(Object object) {
    return castOptionalStringObjectMap(object, true);
  }

  /**
   * Create a new map with lowercase keys
   *
   * @param map         the map
   * @param mapSupplier the map constructor
   * @param <V>         the value type
   * @param <M>         the map type
   *
   * @return the new map
   */
  public static <V, M extends Map<String, V>> M createLowercaseStringMap(Map<String, V> map, Supplier<M> mapSupplier) {
    M newMap = mapSupplier.get();
    map.forEach((k, v) -> newMap.put(k.toLowerCase(Locale.ROOT), v));
    return newMap;
  }

  /**
   * Create a {@link LinkedHashMap} with lowercase keys
   *
   * @param map the map
   * @param <V> the value type
   *
   * @return the new map
   */
  public static <V> Map<String, V> createLowercaseStringMap(Map<String, V> map) {
    return createLowercaseStringMap(map, LinkedHashMap::new);
  }
}
