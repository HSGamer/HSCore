package me.hsgamer.hscore.bukkit.gui.common.event;

import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Collection;

/**
 * The Bukkit implementation of {@link DragEvent}
 */
public class BukkitDragEvent extends BukkitInventoryEvent<InventoryDragEvent> implements BukkitCancellableEvent, DragEvent {
  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitDragEvent(InventoryDragEvent event) {
    super(event);
  }

  @Override
  public Collection<Integer> getSlots() {
    return getEvent().getRawSlots();
  }
}
