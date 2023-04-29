package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.minecraft.item.ItemModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * The {@link ItemMeta} modifier
 */
public interface ItemMetaModifier extends ItemModifier<ItemStack> {
  /**
   * Modify the item meta
   *
   * @param meta            the item meta
   * @param uuid            the unique id
   * @param stringReplacers the string replacers
   *
   * @return the modified item meta
   */
  @NotNull
  ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers);

  /**
   * Load the modifier from the item meta
   *
   * @param meta the item meta
   *
   * @see #loadFromItem(ItemStack)
   */
  boolean loadFromItemMeta(ItemMeta meta);

  @Override
  default @NotNull ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    ItemMeta itemMeta = original.getItemMeta();
    if (itemMeta != null) {
      original.setItemMeta(this.modifyMeta(itemMeta, uuid, stringReplacers));
    }
    return original;
  }

  @Override
  default boolean loadFromItem(ItemStack item) {
    if (item.hasItemMeta()) {
      return this.loadFromItemMeta(item.getItemMeta());
    }
    return false;
  }
}
