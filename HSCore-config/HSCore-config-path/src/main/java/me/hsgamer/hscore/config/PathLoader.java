package me.hsgamer.hscore.config;

import java.util.Arrays;

/**
 * A class that loads config paths on the config instance
 */
public final class PathLoader {
  private PathLoader() {
    // EMPTY
  }

  /**
   * Load the config path that's in the config instance
   *
   * @param config config instance to load
   * @param <C>    config instance's class type
   */
  public static <C extends Configuration> void loadPath(C config) {
    Arrays.stream(config.getClass().getDeclaredFields())
      .filter(field -> ConfigPath.class.isAssignableFrom(field.getType()))
      .forEach(field -> {
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
}
