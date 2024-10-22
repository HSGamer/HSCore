package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;

/**
 * The User Interface
 */
public interface UI extends Initializable, Updatable {
  /**
   * Handle the event
   *
   * @param event the event
   */
  void handleEvent(Object event);
}
