package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.proxy.defaulthandler.DefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.NewJavaDefaultMethodHandler;
import me.hsgamer.hscore.config.proxy.defaulthandler.OldJavaDefaultMethodHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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

  private final Class<T> clazz;
  private final Config config;

  ConfigInvocationHandler(Class<T> clazz, Config config) {
    this.clazz = clazz;
    this.config = config;
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
    } else if (name.startsWith("get")) {

    } else if (name.startsWith("set")) {

    } else if (method.isDefault()) {
      return DEFAULT_METHOD_HANDLER.invoke(proxy, method, args);
    }
    return null;
  }
}
