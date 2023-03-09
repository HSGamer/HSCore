package me.hsgamer.hscore.minecraft.gui.event;

/**
 * The event when a player clicks on the UI
 */
public interface ClickEvent extends ViewerEvent, CancellableEvent {
  /**
   * Get the slot
   *
   * @return the slot
   */
  int getSlot();

  /**
   * Check if the buttons can be executed
   *
   * @return true if the buttons can be executed
   */
  boolean isButtonExecute();

  /**
   * Set if the buttons can be executed
   *
   * @param buttonExecute true if the buttons can be executed
   */
  void setButtonExecute(boolean buttonExecute);
}
