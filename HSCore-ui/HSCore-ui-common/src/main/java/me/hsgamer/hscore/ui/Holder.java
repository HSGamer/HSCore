package me.hsgamer.hscore.ui;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The holder for all displays
 */
public interface Holder {

  /**
   * Create a display with the unique id
   *
   * @param uuid the unique id
   * @return the display
   */
  Display createDisplay(UUID uuid);

  /**
   * Get the display for the unique id
   *
   * @param uuid the unique id
   * @return the display
   */
  Display getDisplay(UUID uuid);

  /**
   * Update all displays
   */
  void updateAll();

  /**
   * Add an event consumer
   *
   * @param eventClass    the event class
   * @param eventConsumer the event consumer
   * @param <T>           the type of the event
   */
  <T> void addEventConsumer(Class<T> eventClass, Consumer<T> eventConsumer);

  /**
   * Handle the event
   *
   * @param event the event
   */
  void handleEvent(Object event);
}
