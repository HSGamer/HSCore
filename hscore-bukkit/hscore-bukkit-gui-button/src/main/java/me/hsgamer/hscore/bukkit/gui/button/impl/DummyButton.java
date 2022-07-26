package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Function;

/**
 * The dummy button with only the item stack
 */
public class DummyButton implements Button {
  private final Function<UUID, ItemStack> itemStackFunction;

  /**
   * Create a new button
   *
   * @param itemStackFunction the item stack function
   */
  public DummyButton(Function<UUID, ItemStack> itemStackFunction) {
    this.itemStackFunction = itemStackFunction;
  }

  /**
   * Create a new button
   *
   * @param itemStack the item stack
   */
  public DummyButton(ItemStack itemStack) {
    this(uuid -> itemStack);
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return itemStackFunction.apply(uuid);
  }
}
