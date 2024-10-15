package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotation.Comment;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import me.hsgamer.hscore.config.annotation.StickyValue;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.defaulthandler.DefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.NewJavaDefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.OldJavaDefaultMethodHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

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
  private final boolean stickyValue;

  /**
   * Constructor
   *
   * @param clazz       The interface
   * @param config      The config
   * @param stickyValue True if the value should be sticky (store the value in the cache)
   * @param addDefault  True if the default value should be added to the config
   */
  ConfigInvocationHandler(Class<T> clazz, Config config, boolean stickyValue, boolean addDefault) {
    this.clazz = clazz;
    this.config = config;
    this.stickyValue = stickyValue;

    Stream<ConfigNode> configNodes = Arrays.stream(this.clazz.getDeclaredMethods())
      .sorted(ConfigInvocationHandler::compareMethod)
      .flatMap(method -> this.setupMethod(method).map(Stream::of).orElseGet(Stream::empty));

    if (addDefault) {
      configNodes.forEach(ConfigNode::addDefault);
      this.setupClassComment();
      this.config.save();
    }
  }

  private static int compareMethod(Method method1, Method method2) {
    if (method1.equals(method2) || !method1.isAnnotationPresent(ConfigPath.class) || !method2.isAnnotationPresent(ConfigPath.class)) {
      return 0;
    }
    ConfigPath configPath1 = method1.getAnnotation(ConfigPath.class);
    ConfigPath configPath2 = method2.getAnnotation(ConfigPath.class);
    return Integer.compare(configPath1.priority(), configPath2.priority());
  }

  /**
   * Check if the class is a primitive type
   *
   * @param clazz The class
   *
   * @return True if the class is a primitive type
   */
  private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
    return clazz.isPrimitive() || clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(Byte.class)
      || clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(Short.class)
      || clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Long.class)
      || clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(Double.class);
  }

  /**
   * Check if the method is a void method
   *
   * @param method The method
   *
   * @return True if the method is a void method
   */
  private static boolean isVoidMethod(Method method) {
    return method.getReturnType() == void.class || method.getReturnType() == Void.class;
  }

  /**
   * Set up the class comment
   */
  private void setupClassComment() {
    if (clazz.isAnnotationPresent(Comment.class) && config.getComment().isEmpty()) {
      config.setComment(Arrays.asList(clazz.getAnnotation(Comment.class).value()));
    }
  }

  /**
   * Set up the methods
   *
   * @param method The method
   */
  private Optional<ConfigNode> setupMethod(Method method) {
    if (!method.isDefault() || method.getParameterCount() != 0) {
      return Optional.empty();
    }

    String name = method.getName();
    String methodName;
    if (name.startsWith("get")) {
      methodName = name.substring(3);
    } else if (name.startsWith("is")) {
      methodName = name.substring(2);
    } else {
      methodName = name;
    }
    if (methodName.isEmpty()) {
      return Optional.empty();
    }

    if (!method.isAnnotationPresent(ConfigPath.class)) {
      return Optional.empty();
    }
    ConfigPath configPath = method.getAnnotation(ConfigPath.class);
    String[] path = configPath.value();

    try {
      Object value = DEFAULT_METHOD_HANDLER.invoke(method);
      ConfigNode node = new ConfigNode(
        path, config, DefaultConverterManager.getConverterIfDefault(method.getGenericReturnType(), configPath.converter()), value,
        method.isAnnotationPresent(Comment.class) ? Arrays.asList(method.getAnnotation(Comment.class).value()) : Collections.emptyList(),
        stickyValue || method.isAnnotationPresent(StickyValue.class)
      );
      nodes.put(methodName, node);
      return Optional.of(node);
    } catch (Throwable e) {
      throw new IllegalStateException("Failed to setup method " + method.getName(), e);
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();
    if ((name.equals("getConfig") || name.equals("config")) && !method.isDefault() && method.getParameterCount() == 0 && method.getReturnType().isInstance(config)) {
      return config;
    } else if (name.equals("toString")) {
      return this.clazz.toString();
    } else if (name.equals("hashCode")) {
      return this.clazz.hashCode();
    } else if (name.equals("equals")) {
      return proxy == args[0];
    } else if ((name.equals("reloadConfig") || name.equals("reload")) && !method.isDefault() && method.getParameterCount() == 0) {
      config.reload();
      nodes.values().forEach(ConfigNode::clearCache);
      return null;
    } else if (!isVoidMethod(method) && method.isDefault() && method.getParameterCount() == 0 && method.isAnnotationPresent(ConfigPath.class)) {
      String methodName;
      if (name.startsWith("get")) {
        methodName = name.substring(3);
      } else if (name.startsWith("is")) {
        methodName = name.substring(2);
      } else {
        methodName = name;
      }
      if (nodes.containsKey(methodName)) {
        Object value = nodes.get(methodName).getValue();
        if ((isPrimitiveOrWrapper(method.getReturnType()) && isPrimitiveOrWrapper(value.getClass())) || method.getReturnType().isInstance(value)) {
          return value;
        }
      }
    } else if (isVoidMethod(method) && !method.isDefault() && method.getParameterCount() == 1) {
      String methodName;
      if (name.startsWith("set")) {
        methodName = name.substring(3);
      } else {
        methodName = name;
      }
      if (nodes.containsKey(methodName)) {
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
