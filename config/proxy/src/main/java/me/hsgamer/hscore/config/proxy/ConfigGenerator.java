package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;

import java.lang.reflect.Proxy;

/**
 * The main class of the config proxy system.
 * Use this class to create a proxied interface with a config.
 */
public final class ConfigGenerator {
  private ConfigGenerator() {
    // EMPTY
  }

  /**
   * Create a new mapped instance of the class from the config
   *
   * @param clazz       The class to create
   * @param config      The config to use
   * @param setupConfig Whether to set up the config
   * @param stickyValue True if the value should be sticky (keep the value in the cache)
   * @param <T>         The class type
   * @param addDefault  True if the default value should be added to the config
   *
   * @return The new instance
   */
  public static <T> T newInstance(Class<T> clazz, Config config, boolean setupConfig, boolean stickyValue, boolean addDefault) {
    if (setupConfig) {
      config.setup();
    }
    Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConfigInvocationHandler<>(clazz, config, stickyValue, addDefault));
    if (clazz.isInstance(object)) {
      return clazz.cast(object);
    } else {
      throw new IllegalArgumentException("The class " + clazz.getName() + " is not an instance of " + object.getClass().getName());
    }
  }


  /**
   * Create a new mapped instance of the class from the config
   *
   * @param clazz       The class to create
   * @param config      The config to use
   * @param setupConfig Whether to set up the config
   * @param stickyValue True if the value should be sticky (keep the value in the cache)
   * @param <T>         The class type
   *
   * @return The new instance
   */
  public static <T> T newInstance(Class<T> clazz, Config config, boolean setupConfig, boolean stickyValue) {
    return newInstance(clazz, config, setupConfig, stickyValue, true);
  }

  /**
   * Create a new mapped instance of the class from the config
   *
   * @param clazz       The class to create
   * @param config      The config to use
   * @param setupConfig Whether to set up the config
   * @param <T>         The class type
   *
   * @return The new instance
   */
  public static <T> T newInstance(Class<T> clazz, Config config, boolean setupConfig) {
    return newInstance(clazz, config, setupConfig, false);
  }

  /**
   * Create a new mapped instance of the class from the config, also set up the config
   *
   * @param clazz  The class to create
   * @param config The config to use
   * @param <T>    The class type
   *
   * @return The new instance
   */
  public static <T> T newInstance(Class<T> clazz, Config config) {
    return newInstance(clazz, config, true);
  }
}
