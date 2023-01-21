package me.hsgamer.hscore.ui.property;

/**
 * The interface for some classes than can be initialized
 */
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
