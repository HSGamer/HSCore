package me.hsgamer.hscore.config.proxy.defaulthandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface DefaultMethodHandler {
  Object invoke(Object proxy, Method method, Object... args) throws Throwable;

  default Object invoke(Method method, Object... args) throws Throwable {
    Class<?> clazz = method.getDeclaringClass();
    Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (o, m, args1) -> null);
    return invoke(proxy, method, args);
  }
}
