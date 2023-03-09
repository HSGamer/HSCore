package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The viewer event for Minestom
 */
public interface MinestomViewerEvent extends ViewerEvent {
  /**
   * Get the Minestom event
   *
   * @return the Minestom event
   */
  PlayerEvent getEvent();

  @NotNull
  @Override
  default UUID getViewerID() {
    return getEvent().getPlayer().getUuid();
  }
}
