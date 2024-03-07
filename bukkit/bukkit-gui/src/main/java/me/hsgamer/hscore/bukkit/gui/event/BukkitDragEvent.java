package me.hsgamer.hscore.bukkit.gui.event;

import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * The Drag event
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
}
