package me.hsgamer.hscore.bukkit.gui.common.event;

import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Collection;

/**
 * The drag event
 */
public class BukkitDragEvent extends BukkitInventoryEvent<InventoryDragEvent> implements BukkitCancellableEvent {
  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitDragEvent(InventoryDragEvent event) {
    super(event);
  }

  /**
   * Get the raw slots
   *
   * @return the raw slots
   */
  public Collection<Integer> getRawSlots() {
    return getEvent().getRawSlots();
  }
}
