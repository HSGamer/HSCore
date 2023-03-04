package me.hsgamer.hscore.bukkit.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * The Bukkit implementation of {@link ClickEvent}
 */
public class BukkitClickEvent extends BukkitInventoryEvent<InventoryClickEvent> implements BukkitCancellableEvent, ClickEvent {
  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitClickEvent(InventoryClickEvent event) {
    super(event);
  }

  @Override
  public int getSlot() {
    return event.getRawSlot();
  }
}
