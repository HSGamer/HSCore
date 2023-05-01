package me.hsgamer.hscore.config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The path string to use in {@link Config}
 */
public class PathString {
  /**
   * The root path
   */
  public static final PathString ROOT = new PathString();

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
    Map<String, Object> result = new LinkedHashMap<>();
    map.forEach((pathString, object) -> result.put(toPath(separator, pathString), object));
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
    Map<PathString, Object> result = new LinkedHashMap<>();
    map.forEach((path, object) -> result.put(toPathString(separator, path), object));
    return result;
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
}
