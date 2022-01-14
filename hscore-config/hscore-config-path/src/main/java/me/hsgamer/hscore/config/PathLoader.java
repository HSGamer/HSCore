package me.hsgamer.hscore.config;

import me.hsgamer.hscore.config.path.ConfigPath;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that loads config paths on the config instance
 */
public final class PathLoader {
  private PathLoader() {
    // EMPTY
  }

  /**
   * Get the path fields of the config instance
   *
   * @param config the config instance
   *
   * @return the path fields
   */
  public static List<Field> getPathFields(Config config) {
    return Arrays.stream(config.getClass().getDeclaredFields())
      .filter(field -> ConfigPath.class.isAssignableFrom(field.getType()))
      .collect(Collectors.toList());
  }

  /**
   * Load the config paths that are in the config instance
   *
   * @param config config instance to load
   */
  public static void loadPath(Config config) {
    getPathFields(config).forEach(field -> {
      final boolean accessible = field.isAccessible();
      try {
        field.setAccessible(true);
        ((ConfigPath<?>) field.get(config)).setConfig(config);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    });
  }

  /**
   * Reload the config paths that are in the config instance
   *
   * @param config config instance to reload
   */
  public static void reloadPath(Config config) {
    getPathFields(config).forEach(field -> {
      final boolean accessible = field.isAccessible();
      try {
        field.setAccessible(true);
        ((ConfigPath<?>) field.get(config)).reload();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    });
  }
}
