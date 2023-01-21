package me.hsgamer.hscore.ui;

import org.jetbrains.annotations.NotNull;

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
  @NotNull
  protected abstract D newDisplay(UUID uuid);

  /**
   * Called when the display is removed
   *
   * @param display the display
   */
  protected void onRemoveDisplay(@NotNull D display) {
    // EMPTY
  }

  @Override
  @NotNull
  public D createDisplay(@NotNull UUID uuid) {
    return displayMap.computeIfAbsent(uuid, uuid1 -> {
      D display = newDisplay(uuid1);
      display.init();
      return display;
    });
  }

  @Override
  public void removeDisplay(@NotNull UUID uuid) {
    Optional.ofNullable(displayMap.remove(uuid)).ifPresent(display -> {
      onRemoveDisplay(display);
      display.stop();
    });
  }

  @Override
  public void removeAllDisplay() {
    displayMap.values().forEach(display -> {
      onRemoveDisplay(display);
      display.stop();
    });
    displayMap.clear();
  }

  @Override
  public Optional<@NotNull D> getDisplay(@NotNull UUID uuid) {
    return Optional.ofNullable(displayMap.get(uuid));
  }

  @Override
  public void update() {
    displayMap.values().forEach(D::update);
  }

  @Override
  public <T> void addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer) {
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
  public void clearEventConsumer(@NotNull Class<?> eventClass) {
    classListMap.remove(eventClass);
  }

  @Override
  public void clearAllEventConsumer() {
    classListMap.clear();
  }

  @Override
  public <E> void handleEvent(@NotNull Class<E> eventClass, @NotNull E event) {
    Optional
      .ofNullable(classListMap.get(eventClass))
      .ifPresent(list -> list.forEach(consumer -> consumer.accept(event)));
  }
}
