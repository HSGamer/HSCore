package me.hsgamer.hscore.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A variant of {@link PathString} that is case-insensitive.
 * Useful for config maps that need to be case-insensitive.
 */
public class CaseInsensitivePathString {
  private final PathString pathString;

  /**
   * Create a new path string
   *
   * @param pathString the path string
   */
  public CaseInsensitivePathString(PathString pathString) {
    this.pathString = pathString;
  }

  /**
   * Create a new path
   *
   * @param path the path
   */
  public CaseInsensitivePathString(String... path) {
    this(new PathString(path));
  }

  /**
   * Convert a map of {@link PathString} to a map of {@link CaseInsensitivePathString}
   *
   * @param map the map
   *
   * @return the converted map
   */
  public static Map<CaseInsensitivePathString, Object> toCaseInsensitiveMap(Map<PathString, Object> map) {
    return map.entrySet().stream()
      .collect(
        Collectors.toMap(
          entry -> new CaseInsensitivePathString(entry.getKey()),
          Map.Entry::getValue,
          (a, b) -> b,
          LinkedHashMap::new
        )
      );
  }

  /**
   * Get the original {@link PathString}
   *
   * @return the original {@link PathString}
   */
  public PathString getPathString() {
    return pathString;
  }

  /**
   * Get the original path
   *
   * @return the original path
   */
  public String[] getPath() {
    return pathString.getPath();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CaseInsensitivePathString that = (CaseInsensitivePathString) o;
    String[] thatPath = that.pathString.getPath();
    String[] thisPath = this.pathString.getPath();
    if (thatPath.length != thisPath.length) return false;
    for (int i = 0; i < thatPath.length; i++) {
      if (!thatPath[i].equalsIgnoreCase(thisPath[i])) return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    String[] thisPath = this.pathString.getPath();
    Object[] lowerPath = new String[thisPath.length];
    for (int i = 0; i < thisPath.length; i++) {
      lowerPath[i] = thisPath[i].toLowerCase();
    }
    return Objects.hash(lowerPath);
  }
}
