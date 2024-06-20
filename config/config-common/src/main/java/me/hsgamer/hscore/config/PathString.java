package me.hsgamer.hscore.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The utility class for path in {@link Config}
 */
public interface PathString {
  /**
   * The default separator
   */
  String DEFAULT_SEPARATOR = ".";

  /**
   * Get the path as array
   *
   * @param path the path
   *
   * @return the path as array
   */
  static String[] asArray(String path) {
    return new String[]{path};
  }

  /**
   * Concatenate two paths
   *
   * @param a1 the first path
   * @param a2 the second path
   *
   * @return the concatenated path
   */
  static String[] concat(String[] a1, String[] a2) {
    String[] result = new String[a1.length + a2.length];
    System.arraycopy(a1, 0, result, 0, a1.length);
    System.arraycopy(a2, 0, result, a1.length, a2.length);
    return result;
  }

  /**
   * Join the path with the separator
   *
   * @param separator the separator
   * @param path      the path
   *
   * @return the joined path
   */
  static String join(String separator, String... path) {
    return String.join(separator, path);
  }

  /**
   * Split the path with the separator
   *
   * @param separator the separator
   * @param path      the path
   *
   * @return the split path
   */
  static String[] split(String separator, String path) {
    return path.split(separator);
  }

  /**
   * Join the path with the separator
   *
   * @param separator the separator
   * @param map       the map containing the path
   *
   * @return the map containing the joined path
   */
  static Map<String, Object> join(String separator, Map<String[], Object> map) {
    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    for (Map.Entry<String[], Object> entry : map.entrySet()) {
      result.put(join(separator, entry.getKey()), entry.getValue());
    }
    return result;
  }

  /**
   * Split the path with the separator
   *
   * @param separator the separator
   * @param map       the map containing the path
   *
   * @return the map containing the split path
   */
  static Map<String[], Object> split(String separator, Map<String, Object> map) {
    LinkedHashMap<String[], Object> result = new LinkedHashMap<>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      result.put(split(separator, entry.getKey()), entry.getValue());
    }
    return result;
  }

  /**
   * Join the path with the default separator
   *
   * @param path the path
   *
   * @return the joined path
   */
  static String joinDefault(String... path) {
    return join(DEFAULT_SEPARATOR, path);
  }

  /**
   * Split the path with the default separator
   *
   * @param path the path
   *
   * @return the split path
   */
  static String[] splitDefault(String path) {
    return split(DEFAULT_SEPARATOR, path);
  }

  /**
   * Join the path with the default separator
   *
   * @param map the map containing the path
   *
   * @return the map containing the joined path
   */
  static Map<String, Object> joinDefault(Map<String[], Object> map) {
    return join(DEFAULT_SEPARATOR, map);
  }

  /**
   * Split the path with the default separator
   *
   * @param map the map containing the path
   *
   * @return the map containing the split path
   */
  static Map<String[], Object> splitDefault(Map<String, Object> map) {
    return split(DEFAULT_SEPARATOR, map);
  }
}
