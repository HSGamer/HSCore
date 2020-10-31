package me.hsgamer.hscore.ui;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseHolder<D extends Display> implements Holder<D> {
  private final Map<Class<?>, List<Consumer<Object>>> classListMap = new HashMap<>();
  private final HashMap<UUID, D> displayMap = new HashMap<>();

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
    D display = newDisplay(uuid);
    displayMap.put(uuid, display);
    return display;
  }

  @Override
  public void removeDisplay(UUID uuid) {
    Optional.ofNullable(displayMap.remove(uuid)).ifPresent(D::close);
  }

  @Override
  public Optional<D> getDisplay(UUID uuid) {
    return Optional.ofNullable(displayMap.get(uuid));
  }

  @Override
  public void updateAll() {
    displayMap.values().forEach(D::update);
  }

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
  public void clearEventConsumer(Class<?> eventClass) {
    classListMap.remove(eventClass);
  }

  @Override
  public void handleEvent(Object event) {
    Optional
      .ofNullable(classListMap.get(event.getClass()))
      .ifPresent(list -> list.forEach(consumer -> consumer.accept(event)));
  }
}
