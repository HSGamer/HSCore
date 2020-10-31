package me.hsgamer.hscore.ui;

import java.util.UUID;

/**
 * The display
 */
public interface Display {

  /**
   * Initialize the display
   */
  void init();

  /**
   * Update the display
   */
  void update();

  /**
   * Close the display
   */
  void close();

  /**
   * Handle the event
   *
   * @param event the event
   */
  default void handleEvent(Object event) {
    getHolder().handleEvent(event);
  }

  /**
   * Get the holder for the display
   *
   * @return the holder
   */
  Holder<?> getHolder();

  /**
   * Get the unique id for the display
   *
   * @return the unique id
   */
  UUID getUniqueId();
}
