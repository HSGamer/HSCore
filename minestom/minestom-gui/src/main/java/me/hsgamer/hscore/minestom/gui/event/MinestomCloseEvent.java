package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;

/**
 * The close event for Minestom
 */
public class MinestomCloseEvent extends MinestomEvent<InventoryCloseEvent> implements CloseEvent, MinestomViewerEvent {
  private boolean removeDisplay = true;

  /**
   * Create a new event
   *
   * @param event the Minestom event
   */
  public MinestomCloseEvent(InventoryCloseEvent event) {
    super(event);
  }

  @Override
  public boolean isRemoveDisplay() {
    return removeDisplay;
  }

  @Override
  public void setRemoveDisplay(boolean removeDisplay) {
    this.removeDisplay = removeDisplay;
  }
}
