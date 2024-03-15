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
  public void stop() {
    clearAllEventConsumer();
    removeAllDisplay();
  }

  /**
   * Add an event consumer
   *
   * @param eventClass    the class of the event
   * @param eventConsumer the consumer
   * @param <T>           the type of the event
   */
  public <T> void addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer) {
    classListMap
      .computeIfAbsent(eventClass, clazz -> new ArrayList<>())
      .add(o -> eventConsumer.accept(eventClass.cast(o)));
  }

  /**
   * Clear the event consumer
   *
   * @param eventClass the class of the event
   */
  public void clearEventConsumer(@NotNull Class<?> eventClass) {
    classListMap.remove(eventClass);
  }

  /**
   * Clear all event consumers
   */
  public void clearAllEventConsumer() {
    classListMap.clear();
  }

  /**
   * Handle the event
   *
   * @param eventClass the class of the event
   * @param event      the event
   */
  public void handleEvent(@NotNull Class<?> eventClass, @NotNull Object event) {
    if (!classListMap.containsKey(eventClass)) return;
    if (!eventClass.isInstance(event)) {
      throw new IllegalArgumentException("The event is not an instance of " + eventClass.getName());
    }
    classListMap.get(eventClass).forEach(consumer -> consumer.accept(event));
  }

  @Override
  public <E> void handleEvent(@NotNull E event) {
    Set<Class<?>> eventClassSet = new HashSet<>();
    Queue<Class<?>> eventClassQueue = new LinkedList<>();
    eventClassQueue.add(event.getClass());
    while (true) {
      Class<?> currentClass = eventClassQueue.poll();
      if (currentClass == null) break;
      if (!eventClassSet.add(currentClass)) continue;
      handleEvent(currentClass, event);
      Optional.ofNullable(currentClass.getSuperclass()).ifPresent(eventClassQueue::add);
      eventClassQueue.addAll(Arrays.asList(currentClass.getInterfaces()));
    }
  }
}
