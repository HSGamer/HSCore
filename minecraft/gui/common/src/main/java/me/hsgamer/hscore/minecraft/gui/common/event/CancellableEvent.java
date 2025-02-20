package me.hsgamer.hscore.minecraft.gui.common.event;

/**
 * The event that can be cancelled
 */
public interface CancellableEvent {
  /**
   * Check if the event is cancelled
   *
   * @return true if cancelled
   */
  boolean isCancelled();

  /**
   * Set the event to be cancelled
   *
   * @param cancelled true if cancelled
   */
  void setCancelled(final boolean cancelled);
}
