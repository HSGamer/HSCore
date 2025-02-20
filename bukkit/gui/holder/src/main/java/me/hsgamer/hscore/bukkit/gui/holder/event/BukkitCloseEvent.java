package me.hsgamer.hscore.bukkit.gui.holder.event;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitInventoryEvent;
import me.hsgamer.hscore.minecraft.gui.holder.event.CloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * The Bukkit implementation of {@link CloseEvent}
 */
public class BukkitCloseEvent extends BukkitInventoryEvent<InventoryCloseEvent> implements CloseEvent {
  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitCloseEvent(InventoryCloseEvent event) {
    super(event);
  }
}
