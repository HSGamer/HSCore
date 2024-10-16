package me.hsgamer.hscore.ui;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * The base implementation for {@link UI}
 */
public abstract class BaseUI implements UI {
  protected final UUID uuid;
  private final Map<Class<?>, List<Consumer<Object>>> classListMap = new HashMap<>();

  /**
   * Create a new UI
   *
   * @param uuid the unique id of the UI
   */
  protected BaseUI(UUID uuid) {
    this.uuid = uuid;
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
  public void stop() {
    clearAllEventConsumer();
  }

  @Override
  public UUID getUniqueId() {
    return uuid;
  }

  @Override
  public void handleEvent(@NotNull Object event) {
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
