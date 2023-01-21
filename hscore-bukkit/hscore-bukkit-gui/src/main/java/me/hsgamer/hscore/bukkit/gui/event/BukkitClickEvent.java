package me.hsgamer.hscore.bukkit.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The Bukkit implementation of {@link ClickEvent}
 */
public class BukkitClickEvent implements ClickEvent {
  private final InventoryClickEvent event;

  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitClickEvent(InventoryClickEvent event) {
    this.event = event;
  }

  /**
   * Get the Bukkit event
   *
   * @return the Bukkit event
   */
  public InventoryClickEvent getEvent() {
    return event;
  }

  @Override
  public boolean isCancelled() {
    return event.isCancelled();
  }

  @Override
  public void setCancelled(boolean cancelled) {
    event.setCancelled(cancelled);
  }

  @Override
  public int getSlot() {
    return event.getRawSlot();
  }

  @Override
  public @NotNull UUID getViewerID() {
    return event.getWhoClicked().getUniqueId();
  }
}
