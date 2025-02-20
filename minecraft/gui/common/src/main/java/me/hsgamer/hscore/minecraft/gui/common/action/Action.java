package me.hsgamer.hscore.minecraft.gui.common.action;

import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;

/**
 * The action for the GUI
 */
public interface Action {
  /**
   * The empty action
   */
  Action EMPTY = new Action() {
  };

  /**
   * Handle the click event
   *
   * @param event the click event
   */
  default void handleClick(ClickEvent event) {

  }

  /**
   * Handle the drag event
   *
   * @param event the drag event
   */
  default void handleDrag(DragEvent event) {

  }
}
