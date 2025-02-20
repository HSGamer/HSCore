package me.hsgamer.hscore.bukkit.gui.common.event;

import me.hsgamer.hscore.minecraft.gui.common.event.ViewerEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The Bukkit inventory event
 *
 * @param <T> the type of the Bukkit event
 */
public class BukkitInventoryEvent<T extends InventoryEvent> implements ViewerEvent {
  /**
   * The Bukkit event
   */
  protected final T event;

  /**
   * Create a new event
   *
   * @param event the Bukkit event
   */
  public BukkitInventoryEvent(T event) {
    this.event = event;
  }

  /**
   * Get the Bukkit event
   *
   * @return the Bukkit event
   */
  public T getEvent() {
    return event;
  }

  @Override
  public @NotNull UUID getViewerID() {
    return event.getView().getPlayer().getUniqueId();
  }
}
