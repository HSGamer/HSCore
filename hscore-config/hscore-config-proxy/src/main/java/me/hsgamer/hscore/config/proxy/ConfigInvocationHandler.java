package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import me.hsgamer.hscore.config.proxy.defaulthandler.DefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.NewJavaDefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.OldJavaDefaultMethodHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The internal invocation handler to map the interface to the config
 *
 * @param <T> The type of the interface
 */
public class ConfigInvocationHandler<T> implements InvocationHandler {
  private static final DefaultMethodHandler DEFAULT_METHOD_HANDLER;

  static {
    final float version = Float.parseFloat(System.getProperty("java.class.version"));
    if (version <= 52) {
      DEFAULT_METHOD_HANDLER = new OldJavaDefaultMethodHandler();
    } else {
      DEFAULT_METHOD_HANDLER = new NewJavaDefaultMethodHandler();
    }
  }

  private final Map<String, ConfigNode> nodes = new HashMap<>();
  private final Class<T> clazz;
  private final Config config;

  /**
   * Constructor
   *
   * @param clazz  The interface
   * @param config The config
   */
  ConfigInvocationHandler(Class<T> clazz, Config config) {
    this.clazz = clazz;
    this.config = config;
    for (Method method : clazz.getDeclaredMethods()) {
      this.setupMethod(method);
    }
    config.save();
  }

  /**
   * Set up the methods
   *
   * @param method The method
   */
  private void setupMethod(Method method) {
    if (!method.isDefault()) {
      return;
    }
    if (method.getParameterCount() != 0) {
      return;
    }

    String name = method.getName();
    if (!name.startsWith("get")) {
      return;
    }
    String methodName = name.substring(3);
    if (methodName.isEmpty()) {
      return;
    }

    if (!method.isAnnotationPresent(ConfigPath.class)) {
      return;
    }
    ConfigPath configPath = method.getAnnotation(ConfigPath.class);

    try {
      Object value = DEFAULT_METHOD_HANDLER.invoke(method);
      ConfigNode node = new ConfigNode(configPath.value(), config, configPath.converter(), value);
      nodes.put(methodName, node);
    } catch (Throwable e) {
      throw new IllegalStateException("Failed to setup method " + method.getName(), e);
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();
    if (name.equals("getConfig") && method.getReturnType().isInstance(config)) {
      return config;
    } else if (name.equals("toString")) {
      return this.clazz.toString();
    } else if (name.equals("hashCode")) {
      return this.clazz.hashCode();
    } else if (name.equals("equals")) {
      return proxy == args[0];
    } else if (name.startsWith("get") && method.isDefault() && method.isAnnotationPresent(ConfigPath.class)) {
      String methodName = name.substring(3);
      if (!methodName.isEmpty() && nodes.containsKey(methodName)) {
        Object value = nodes.get(methodName).getValue();
        if (method.getReturnType().isInstance(value)) {
          return value;
        }
      }
    } else if (name.startsWith("set") && !method.isDefault() && args.length == 1) {
      String methodName = name.substring(3);
      if (!methodName.isEmpty() && nodes.containsKey(methodName)) {
        nodes.get(methodName).setValue(args[0]);
        config.save();
        return null;
      }
    }
    if (method.isDefault()) {
      return DEFAULT_METHOD_HANDLER.invoke(proxy, method, args);
    }
    throw new UnsupportedOperationException("Method " + method.getName() + " is not supported");
  }
}
