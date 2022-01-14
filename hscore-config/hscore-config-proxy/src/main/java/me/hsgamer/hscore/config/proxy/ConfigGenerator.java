package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;

import java.lang.reflect.Proxy;

public final class ConfigGenerator {
  private ConfigGenerator() {
    // EMPTY
  }

  public static <T> T newInstance(Class<T> clazz, Config config) {
    Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConfigInvocationHandler<>(clazz, config));
    if (clazz.isInstance(object)) {
      return clazz.cast(object);
    } else {
      throw new IllegalArgumentException("The class " + clazz.getName() + " is not an instance of " + object.getClass().getName());
    }
  }
}
