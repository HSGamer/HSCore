package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;

/**
 * The User Interface
 */
public interface UI extends Initializable {
  /**
   * Handle the event
   *
   * @param event the event
   */
  void handleEvent(Object event);
}
