package me.hsgamer.hscore.bukkit.gui.object;

import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.bukkit.inventory.ItemStack;

/**
 * The Bukkit item
 */
public class BukkitItem implements Item {
  private final ItemStack itemStack;

  /**
   * Create a new Bukkit item
   *
   * @param itemStack the item stack
   */
  public BukkitItem(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  /**
   * Get the item stack
   *
   * @return the item stack
   */
  public ItemStack getItemStack() {
    return itemStack;
  }
}
