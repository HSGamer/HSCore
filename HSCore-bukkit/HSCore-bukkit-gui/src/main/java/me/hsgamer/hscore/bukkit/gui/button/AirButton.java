package me.hsgamer.hscore.bukkit.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * The air button
 */
public class AirButton extends SimpleButton {
  /**
   * Create a new button
   *
   * @param consumer the consumer
   */
  public AirButton(BiConsumer<UUID, InventoryClickEvent> consumer) {
    super(null, consumer);
  }
}
