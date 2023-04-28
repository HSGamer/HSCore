package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.minecraft.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The item builder
 */
public class BukkitItemBuilder extends ItemBuilder<ItemStack> {
  private ItemStack defaultItemStack;

  /**
   * Create a new builder
   */
  public BukkitItemBuilder() {
    super();
  }

  /**
   * Create a new builder with the default item
   *
   * @param defaultItemStack the default item
   */
  public BukkitItemBuilder(ItemStack defaultItemStack) {
    this.defaultItemStack = defaultItemStack;
  }

  /**
   * Create a new builder with the default item
   *
   * @param material the {@link Material} of the default item
   */
  public BukkitItemBuilder(Material material) {
    this.defaultItemStack = new ItemStack(material);
  }

  @Override
  protected @NotNull ItemStack getDefaultItem() {
    return defaultItemStack == null ? new ItemStack(Material.STONE) : defaultItemStack.clone();
  }
}
