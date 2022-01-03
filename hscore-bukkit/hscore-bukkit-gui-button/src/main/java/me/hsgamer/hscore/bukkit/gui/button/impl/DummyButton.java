package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The dummy button with only the item stack
 */
public class DummyButton implements Button {
  private final ItemStack itemStack;

  /**
   * Create a new button
   *
   * @param itemStack the item stack
   */
  public DummyButton(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return itemStack;
  }
}
