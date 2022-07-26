package me.hsgamer.hscore.bukkit.gui.button.impl;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * The null button, only with action
 */
public class NullButton extends SimpleButton {
  /**
   * Create a new button
   *
   * @param consumer the consumer
   */
  public NullButton(BiConsumer<UUID, InventoryClickEvent> consumer) {
    super(uuid -> null, consumer);
  }

  @Override
  public boolean forceSetAction(UUID uuid) {
    return true;
  }
}
