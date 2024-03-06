package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The path string to use in {@link Config}
 */
public class PathString implements Comparable<PathString> {
  /**
   * The root path
   */
  public static final PathString ROOT = new PathString();
  /**
   * The default separator
   */
  public static final String DEFAULT_SEPARATOR = ".";

  private final String[] path;

  /**
   * Create a new path string
   *
   * @param path the path
   */
  public PathString(String... path) {
    this.path = path;
  }

  /**
   * Join the path with the separator
   *
   * @param separator  the separator
   * @param pathString the path string
   *
   * @return the joined path
   */
  public static String toPath(String separator, PathString pathString) {
    return String.join(separator, pathString.getPath());
  }

  /**
   * Split the path with the separator
   *
   * @param separator the separator
   * @param path      the path
   *
   * @return the path string
   */
  public static PathString toPathString(String separator, String path) {
    return new PathString(path.split(Pattern.quote(separator)));
  }

  /**
   * Join the path with the separator
   *
   * @param separator the separator
   * @param map       the map containing the path string
   *
   * @return the map containing the joined path
   */
  public static Map<String, Object> toPathMap(String separator, Map<PathString, Object> map) {
    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    for (Map.Entry<PathString, Object> entry : map.entrySet()) {
      result.put(toPath(separator, entry.getKey()), entry.getValue());
    }
    return result;
  }

  /**
   * Split the path with the separator
   *
   * @param separator the separator
   * @param map       the map containing the path
   *
   * @return the map containing the path string
   */
  public static Map<PathString, Object> toPathStringMap(String separator, Map<String, Object> map) {
    LinkedHashMap<PathString, Object> result = new LinkedHashMap<>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      result.put(toPathString(separator, entry.getKey()), entry.getValue());
    }
    return result;
  }

  /**
   * Join the path with the default separator
   *
   * @param pathString the path string
   *
   * @return the joined path
   */
  public static String toPath(PathString pathString) {
    return toPath(DEFAULT_SEPARATOR, pathString);
  }

  /**
   * Split the path with the default separator
   *
   * @param path the path
   *
   * @return the path string
   */
  public static PathString toPathString(String path) {
    return toPathString(DEFAULT_SEPARATOR, path);
  }

  /**
   * Join the path with the default separator
   *
   * @param map the map containing the path string
   *
   * @return the map containing the joined path
   */
  public static Map<String, Object> toPathMap(Map<PathString, Object> map) {
    return toPathMap(DEFAULT_SEPARATOR, map);
  }

  /**
   * Split the path with the default separator
   *
   * @param map the map containing the path
   *
   * @return the map containing the path string
   */
  public static Map<PathString, Object> toPathStringMap(Map<String, Object> map) {
    return toPathStringMap(DEFAULT_SEPARATOR, map);
  }

  /**
   * Get the path
   *
   * @return the path
   */
  public String[] getPath() {
    return path;
  }

  /**
   * Get the path as object array
   *
   * @return the path as object array
   */
  public Object[] getPathAsObject() {
    return path;
  }

  /**
   * Check if the path is empty. It means that the path is {@link #ROOT}
   *
   * @return true if the path is empty
   */
  public boolean isRoot() {
    return path.length == 0;
  }

  /**
   * Get the last path
   *
   * @return the last path
   */
  public String getLastPath() {
    if (path.length == 0) {
      return "";
    }
    return path[path.length - 1];
  }

  /**
   * Create a new path string by appending the path string to the current path string
   *
   * @param pathString the path string to append
   *
   * @return the new path string
   */
  public PathString append(PathString pathString) {
    String[] newPath = new String[this.path.length + pathString.path.length];
    System.arraycopy(this.path, 0, newPath, 0, this.path.length);
    System.arraycopy(pathString.path, 0, newPath, this.path.length, pathString.path.length);
    return new PathString(newPath);
  }

  /**
   * Create a new path string by appending the path to the current path string
   *
   * @param path the path to append
   *
   * @return the new path string
   */
  public PathString append(String... path) {
    return append(new PathString(path));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PathString that = (PathString) o;
    return Arrays.equals(path, that.path);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(path);
  }

  @Override
  public int compareTo(@NotNull PathString o) {
    int length = Math.min(path.length, o.path.length);
    for (int i = 0; i < length; i++) {
      int compare = path[i].compareTo(o.path[i]);
      if (compare != 0) {
        return compare;
      }
    }
    return Integer.compare(path.length, o.path.length);
  }
}
