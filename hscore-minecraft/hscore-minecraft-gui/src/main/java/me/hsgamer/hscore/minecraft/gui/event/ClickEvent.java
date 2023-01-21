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
}
