package me.hsgamer.hscore.bukkit.gui.common.event;

import me.hsgamer.hscore.minecraft.gui.common.event.CancellableEvent;
import org.bukkit.event.Cancellable;

/**
 * The Bukkit implementation of {@link CancellableEvent}
 */
public interface BukkitCancellableEvent extends CancellableEvent {
  /**
   * Get the Bukkit event
   *
   * @return the Bukkit event
   */
  Cancellable getEvent();

  @Override
  default boolean isCancelled() {
    return getEvent().isCancelled();
  }

  @Override
  default void setCancelled(boolean cancelled) {
    getEvent().setCancelled(cancelled);
  }
}
