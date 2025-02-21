package me.hsgamer.hscore.minecraft.gui.common;

/**
 * An element of the GUI
 */
public interface GUIElement {
  /**
   * Initialize the element. Should be called before adding to the GUI.
   */
  default void init() {
  }

  /**
   * Stop the element. Should be called after removing from the GUI.
   */
  default void stop() {
  }
}
