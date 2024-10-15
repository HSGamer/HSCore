package me.hsgamer.hscore.config.proxy.defaulthandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The handler for default methods in interfaces
 */
public interface DefaultMethodHandler {
  /**
   * Invoke the method
   *
   * @param proxy  the proxy
   * @param method the method
   * @param args   the arguments
   *
   * @return the result
   *
   * @throws Throwable the throwable
   */
  Object invoke(Object proxy, Method method, Object... args) throws Throwable;

  /**
   * Invoke the method using a dummy proxy
   *
   * @param method the method
   * @param args   the arguments
   *
   * @return the result
   *
   * @throws Throwable the throwable
   */
  default Object invoke(Method method, Object... args) throws Throwable {
    Class<?> clazz = method.getDeclaringClass();
    Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (o, m, args1) -> null);
    return invoke(proxy, method, args);
  }
}
