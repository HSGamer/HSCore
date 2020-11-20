package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;

import java.util.Optional;
import java.util.UUID;
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
  D createDisplay(UUID uuid);

  /**
   * Remove a display with the unique id
   *
   * @param uuid the unique id
   */
  void removeDisplay(UUID uuid);

  /**
   * Get the display for the unique id
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  Optional<D> getDisplay(UUID uuid);

  /**
   * Add an event consumer
   *
   * @param eventClass    the event class
   * @param eventConsumer the event consumer
   * @param <T>           the type of the event
   */
  <T> void addEventConsumer(Class<T> eventClass, Consumer<T> eventConsumer);

  /**
   * Clear all event consumers
   *
   * @param eventClass the event class
   */
  void clearEventConsumer(Class<?> eventClass);

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
   * @param event the event
   */
  void handleEvent(Object event);
}
