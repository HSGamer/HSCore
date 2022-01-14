package me.hsgamer.hscore.config.proxy.defaulthandler;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class NewJavaDefaultMethodHandler implements DefaultMethodHandler {
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return MethodHandles.lookup()
      .findSpecial(
        method.getDeclaringClass(),
        method.getName(),
        MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
        method.getDeclaringClass()
      ).bindTo(proxy)
      .invokeWithArguments(args);
  }
}
