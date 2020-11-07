package me.hsgamer.hscore.ui;

/**
 * The interface for some classes than can be updated
 */
public interface Updatable {

  /**
   * Initialize some properties for the update
   */
  void initUpdate();

  /**
   * Update the updatable object
   */
  void update();

  /**
   * Stop the update task
   */
  void stopUpdate();
}
