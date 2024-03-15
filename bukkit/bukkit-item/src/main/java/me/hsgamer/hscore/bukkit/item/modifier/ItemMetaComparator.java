package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.minecraft.item.ItemComparator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * An extension of {@link ItemComparator} for {@link ItemMeta}
 */
public interface ItemMetaComparator extends ItemComparator<ItemStack> {
  /**
   * Compare the modifier of an item meta
   *
   * @param meta           the item meta
   * @param uuid           the unique id
   * @param stringReplacer the string replacer
   *
   * @return true if it matches, otherwise false
   *
   * @see #compare(ItemStack, UUID, StringReplacer)
   */
  boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer);

  @Override
  default boolean compare(@NotNull ItemStack item, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    ItemMeta itemMeta;
    try {
      itemMeta = item.getItemMeta();
    } catch (Exception e) {
      return false;
    }

    if (itemMeta == null) {
      return false;
    }
    return compare(itemMeta, uuid, stringReplacer);
  }
}
