package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.holder.event.OpenEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;

/**
 * The open event for Minestom
 */
public final class MinestomOpenEvent extends MinestomEvent<InventoryOpenEvent> implements OpenEvent, MinestomViewerEvent {
  /**
   * Create a new event
   *
   * @param event the Minestom event
   */
  public MinestomOpenEvent(InventoryOpenEvent event) {
    super(event);
  }
}
