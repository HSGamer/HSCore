package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;

import java.util.UUID;

/**
 * The User Interface
 */
public interface UI extends Initializable, Updatable {
  /**
   * Get the unique id for the UI
   *
   * @return the unique id
   */
  UUID getUniqueId();

  /**
   * Handle the event
   *
   * @param event the event
   */
  void handleEvent(Object event);
}
