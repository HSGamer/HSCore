package me.hsgamer.hscore.bukkit.gui.holder.event;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitInventoryEvent;
import me.hsgamer.hscore.minecraft.gui.holder.event.OpenEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * The Bukkit implementation of {@link OpenEvent}
 */
public class BukkitOpenEvent extends BukkitInventoryEvent<InventoryOpenEvent> implements OpenEvent {
  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitOpenEvent(InventoryOpenEvent event) {
    super(event);
  }
}
