package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.common.event.CancellableEvent;

/**
 * The cancellable event for Minestom
 */
public interface MinestomCancellableEvent extends CancellableEvent {
  /**
   * Get the Minestom event
   *
   * @return the Minestom event
   */
  net.minestom.server.event.trait.CancellableEvent getEvent();

  @Override
  default boolean isCancelled() {
    return getEvent().isCancelled();
  }

  @Override
  default void setCancelled(boolean cancelled) {
    getEvent().setCancelled(cancelled);
  }
}
