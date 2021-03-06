package me.hsgamer.hscore.ui;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A simple implementation of {@link Holder}
 *
 * @param <D> The type of {@link Display}
 */
public abstract class BaseHolder<D extends Display> implements Holder<D> {
  protected final Map<UUID, D> displayMap = new ConcurrentHashMap<>();
  private final Map<Class<?>, List<Consumer<Object>>> classListMap = new HashMap<>();

  /**
   * Make a new display
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  protected abstract D newDisplay(UUID uuid);

  @Override
  public D createDisplay(UUID uuid) {
    return displayMap.computeIfAbsent(uuid, this::newDisplay);
  }

  @Override
  public void removeDisplay(UUID uuid) {
    Optional.ofNullable(displayMap.remove(uuid)).ifPresent(D::stop);
  }

  @Override
  public void removeAllDisplay() {
    displayMap.values().forEach(D::stop);
    displayMap.clear();
  }

  @Override
  public Optional<D> getDisplay(UUID uuid) {
    return Optional.ofNullable(displayMap.get(uuid));
  }

  @Override
  public void update() {
    displayMap.values().forEach(D::update);
  }

  @Override
  public <T> void addEventConsumer(Class<T> eventClass, Consumer<T> eventConsumer) {
    classListMap
      .computeIfAbsent(eventClass, clazz -> new LinkedList<>())
      .add(o -> eventConsumer.accept(eventClass.cast(o)));
  }

  @Override
  public void stop() {
    clearAllEventConsumer();
    removeAllDisplay();
  }

  @Override
  public void clearEventConsumer(Class<?> eventClass) {
    classListMap.remove(eventClass);
  }

  @Override
  public void clearAllEventConsumer() {
    classListMap.clear();
  }

  @Override
  public void handleEvent(Object event) {
    Optional
      .ofNullable(classListMap.get(event.getClass()))
      .ifPresent(list -> list.forEach(consumer -> consumer.accept(event)));
  }
}
