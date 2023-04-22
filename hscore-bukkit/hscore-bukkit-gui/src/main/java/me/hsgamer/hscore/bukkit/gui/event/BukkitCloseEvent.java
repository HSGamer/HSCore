package me.hsgamer.hscore.bukkit.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * The Bukkit implementation of {@link CloseEvent}
 */
public class BukkitCloseEvent extends BukkitInventoryEvent<InventoryCloseEvent> implements CloseEvent {
  private boolean removeDisplay = true;

  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitCloseEvent(InventoryCloseEvent event) {
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
