package me.hsgamer.hscore.config.proxy;

import me.hsgamer.hscore.config.Config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConfigInvocationHandler implements InvocationHandler {
  private final Class<? extends ProxyConfig> clazz;
  private final Config config;

  public ConfigInvocationHandler(Class<? extends ProxyConfig> clazz, Config config) {
    this.clazz = clazz;
    this.config = config;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();
    if (name.equals("getConfig")) {
      return config;
    } else if (name.equals("toString")) {
      return this.clazz.toString();
    } else if (name.equals("hashCode")) {
      return this.clazz.hashCode();
    } else if (name.equals("equals")) {
      return proxy == args[0];
    } else if (name.startsWith("get")) {

    } else if (name.startsWith("set")) {

    }
    return null;
  }
}
