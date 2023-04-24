package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * The {@link ItemMeta} modifier
 */
public interface ItemMetaModifier extends ItemModifier {
  /**
   * Modify the item meta
   *
   * @param meta              the item meta
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return the modified item meta
   */
  @NotNull
  ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap);

  /**
   * Load the modifier from the item meta
   *
   * @param meta the item meta
   *
   * @see #loadFromItemStack(ItemStack)
   */
  boolean loadFromItemMeta(ItemMeta meta);

  @Override
  default @NotNull ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = original.getItemMeta();
    if (itemMeta != null) {
      original.setItemMeta(this.modifyMeta(itemMeta, uuid, stringReplacerMap));
    }
    return original;
  }

  @Override
  default boolean loadFromItemStack(ItemStack itemStack) {
    if (itemStack.hasItemMeta()) {
      return this.loadFromItemMeta(itemStack.getItemMeta());
    }
    return false;
  }
}
