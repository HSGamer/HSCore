package me.hsgamer.hscore.minecraft.gui.event;

/**
 * The event when a player closes the UI
 */
public interface CloseEvent extends ViewerEvent {
  /**
   * Check if the display can be removed
   *
   * @return true if the display can be removed
   */
  boolean isRemoveDisplay();

  /**
   * Set whether the display can be removed
   *
   * @param removeDisplay true if the display can be removed
   */
  void setRemoveDisplay(boolean removeDisplay);
}
