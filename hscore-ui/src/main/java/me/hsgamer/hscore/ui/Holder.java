package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * The holder for all displays
 *
 * @param <D> the type of the display
 */
public interface Holder<D extends Display> extends Initializable, Updatable {
  /**
   * Create a display with the unique id
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  @NotNull
  D createDisplay(@NotNull UUID uuid);

  /**
   * Remove a display with the unique id
   *
   * @param uuid the unique id
   */
  void removeDisplay(@NotNull UUID uuid);

  /**
   * Get the display for the unique id
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  Optional<@NotNull D> getDisplay(@NotNull UUID uuid);

  /**
   * Add an event consumer
   *
   * @param eventClass    the event class
   * @param eventConsumer the event consumer
   * @param <T>           the type of the event
   */
  <T> void addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer);

  /**
   * Clear all event consumers
   *
   * @param eventClass the event class
   */
  void clearEventConsumer(@NotNull Class<?> eventClass);

  /**
   * Clear all event consumers of all events
   */
  void clearAllEventConsumer();

  /**
   * Remove all displays
   */
  void removeAllDisplay();

  /**
   * Handle the event
   *
   * @param eventClass the event class
   * @param event      the event
   */
  void handleEvent(@NotNull Class<?> eventClass, @NotNull Object event);

  /**
   * Handle the event
   *
   * @param event the event
   */
  default <E> void handleEvent(@NotNull E event) {
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

  /**
   * Update the display for the unique id
   *
   * @param uuid the unique id
   */
  default void update(UUID uuid) {
    getDisplay(uuid).ifPresent(Updatable::update);
  }
}
