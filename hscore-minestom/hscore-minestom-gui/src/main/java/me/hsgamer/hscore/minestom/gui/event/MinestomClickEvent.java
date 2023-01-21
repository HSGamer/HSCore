package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The click event for Minestom
 *
 * @param event the event
 */
public record MinestomClickEvent(@NotNull InventoryPreClickEvent event) implements ClickEvent {
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
    return event.getSlot();
  }

  @Override
  public @NotNull UUID getViewerID() {
    return event.getPlayer().getUuid();
  }
}
