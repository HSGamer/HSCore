package me.hsgamer.hscore.config.proxy.defaulthandler;

import java.lang.reflect.Method;

public interface DefaultMethodHandler {
  Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
