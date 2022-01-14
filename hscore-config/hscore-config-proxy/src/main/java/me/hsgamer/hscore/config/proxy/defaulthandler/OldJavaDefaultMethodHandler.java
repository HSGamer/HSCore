package me.hsgamer.hscore.config.proxy.defaulthandler;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class OldJavaDefaultMethodHandler implements DefaultMethodHandler {
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }

    final Class<?> clazz = method.getDeclaringClass();
    return constructor.newInstance(clazz)
      .in(clazz)
      .unreflectSpecial(method, clazz)
      .bindTo(proxy)
      .invokeWithArguments(args);
  }
}
