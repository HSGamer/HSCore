package me.hsgamer.hscore.bukkit.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.OpenEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The Bukkit implementation of {@link OpenEvent}
 */
public class BukkitOpenEvent implements OpenEvent {
  private final InventoryOpenEvent event;

  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitOpenEvent(InventoryOpenEvent event) {
    this.event = event;
  }

  /**
   * Get the Bukkit event
   *
   * @return the Bukkit event
   */
  public InventoryOpenEvent getEvent() {
    return event;
  }

  @Override
  public @NotNull UUID getViewerID() {
    return event.getPlayer().getUniqueId();
  }
}
