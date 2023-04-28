package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import me.hsgamer.hscore.minecraft.item.ItemComparator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * An extension of {@link ItemComparator} for {@link ItemMeta}
 */
public interface ItemMetaComparator extends me.hsgamer.hscore.minecraft.item.ItemComparator<ItemStack> {
  /**
   * Compare the modifier of an item meta
   *
   * @param meta            the item meta
   * @param uuid            the unique id
   * @param stringReplacers the string replacers
   *
   * @return true if it matches, otherwise false
   *
   * @see #compare(ItemStack, UUID, Collection)
   */
  boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers);

  @Override
  default boolean compare(@NotNull ItemStack item, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    ItemMeta itemMeta;
    try {
      itemMeta = item.getItemMeta();
    } catch (Exception e) {
      return false;
    }

    if (itemMeta == null) {
      return false;
    }
    return compare(itemMeta, uuid, stringReplacers);
  }
}
