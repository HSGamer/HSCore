package me.hsgamer.hscore.minestom.gui.event;

import me.hsgamer.hscore.minecraft.gui.event.OpenEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The open event for Minestom
 *
 * @param event the event
 */
public record MinestomOpenEvent(@NotNull InventoryOpenEvent event) implements OpenEvent {
  @Override
  public @NotNull UUID getViewerID() {
    return event.getPlayer().getUuid();
  }
}
