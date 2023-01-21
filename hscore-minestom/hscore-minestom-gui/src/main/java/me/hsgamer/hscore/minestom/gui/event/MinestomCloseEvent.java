package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The close event for Minestom
 *
 * @param event the event
 */
public record MinestomCloseEvent(@NotNull InventoryCloseEvent event) implements CloseEvent {
  @Override
  public @NotNull UUID getViewerID() {
    return event.getPlayer().getUuid();
  }
}
