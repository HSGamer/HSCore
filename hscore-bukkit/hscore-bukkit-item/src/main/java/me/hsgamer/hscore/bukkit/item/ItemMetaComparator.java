package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * An extension of {@link ItemComparator} for {@link ItemMeta}
 */
public interface ItemMetaComparator extends ItemComparator {
  /**
   * Compare the modifier of an item meta
   *
   * @param meta              the item meta
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return true if it matches, otherwise false
   *
   * @see #compare(ItemStack, UUID, Map)
   */
  boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap);

  @Override
  default boolean compare(@NotNull ItemStack itemStack, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta;
    try {
      itemMeta = itemStack.getItemMeta();
    } catch (Exception e) {
      return false;
    }

    if (itemMeta == null) {
      return false;
    }
    return compare(itemMeta, uuid, stringReplacerMap);
  }
}
