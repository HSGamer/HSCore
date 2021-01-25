package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;

import java.util.UUID;

/**
 * The display
 */
public interface Display extends Initializable, Updatable {

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
