package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

/**
 * The click event for Minestom
 */
public class MinestomClickEvent extends MinestomEvent<InventoryPreClickEvent> implements ClickEvent, MinestomCancellableEvent, MinestomViewerEvent {
  /**
   * Create a new event
   *
   * @param event the Minestom event
   */
  public MinestomClickEvent(InventoryPreClickEvent event) {
    super(event);
  }

  @Override
  public int getSlot() {
    return event.getSlot();
  }
}
