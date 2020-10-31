package me.hsgamer.hscore.ui;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseHolder<D extends Display> implements Holder<D> {
  private final Map<Class<?>, List<Consumer<Object>>> classListMap = new HashMap<>();

  @Override
  public <T> void addEventConsumer(Class<T> eventClass, Consumer<T> eventConsumer) {
    classListMap
      .computeIfAbsent(eventClass, clazz -> new ArrayList<>())
      .add(o -> {
        if (eventClass.isInstance(o)) {
          eventConsumer.accept((T) o);
        }
      });
  }

  @Override
  public void handleEvent(Object event) {
    Optional
      .ofNullable(classListMap.get(event.getClass()))
      .ifPresent(list -> list.forEach(consumer -> consumer.accept(event)));
  }
}
