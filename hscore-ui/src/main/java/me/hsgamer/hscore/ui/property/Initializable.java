package me.hsgamer.hscore.ui.property;

public interface Initializable {
  /**
   * Initialize some properties for the object
   */
  default void init() {
    // EMPTY
  }

  /**
   * Stop the object
   */
  default void stop() {
    // EMPTY
  }
}
