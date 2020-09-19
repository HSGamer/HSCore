package me.hsgamer.hscore.config;

import java.lang.reflect.Field;

/**
 * A class that loads config paths on the config instance
 * <p>
 * Usage example:
 * <pre>
 *   public final class TestPlugin extends JavaPlugin {
 *
 *     &#64;Override
 *     public void onEnable() {
 *       final TestConfig testConfig = new TestConfig(this);
 *       ConfigLoader.load(testConfig);
 *       final boolean testBoolean = testConfig.testBoolean.getValue();
 *     }
 *
 *   }
 * </pre>
 */
public final class ConfigLoader {

  private ConfigLoader() {
  }

  /**
   * Load the config path that's in the config instance
   *
   * @param config config instance to load
   * @param <C>    config instance's class type
   */
  public static <C extends Config> void load(C config) {
    final Field[] fields = config.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (BaseConfigPath.class.isAssignableFrom(field.getType())) {
        try {
          ((BaseConfigPath<?>) field.get(config)).setConfig(config);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
