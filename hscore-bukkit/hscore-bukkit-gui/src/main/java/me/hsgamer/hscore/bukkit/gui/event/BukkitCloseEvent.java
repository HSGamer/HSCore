package me.hsgamer.hscore.bukkit.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The Bukkit implementation of {@link CloseEvent}
 */
public class BukkitCloseEvent implements CloseEvent {
  private final InventoryCloseEvent event;

  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitCloseEvent(InventoryCloseEvent event) {
    this.event = event;
  }

  /**
   * Get the Bukkit event
   *
   * @return the Bukkit event
   */
  public InventoryCloseEvent getEvent() {
    return event;
  }

  @Override
  public @NotNull UUID getViewerID() {
    return event.getPlayer().getUniqueId();
  }
}
