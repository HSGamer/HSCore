package me.hsgamer.hscore.bukkit.gui.button.impl;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
    super(new ItemStack(Material.AIR), consumer);
  }
}
