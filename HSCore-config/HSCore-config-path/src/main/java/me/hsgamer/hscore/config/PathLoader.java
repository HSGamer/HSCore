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
  public static <C extends Config> void loadPath(C config) {
    Arrays.stream(config.getClass().getDeclaredFields())
      .filter(field -> BaseConfigPath.class.isAssignableFrom(field.getType()))
      .forEach(field -> {
        try {
          ((BaseConfigPath<?>) field.get(config)).setConfig(config);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      });
  }
}
