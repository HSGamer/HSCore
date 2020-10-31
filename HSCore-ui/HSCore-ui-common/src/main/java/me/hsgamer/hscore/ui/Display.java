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
