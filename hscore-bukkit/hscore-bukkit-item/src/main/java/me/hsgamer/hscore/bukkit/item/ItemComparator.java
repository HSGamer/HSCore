package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * An extension to {@link ItemModifier} to compare the modifier of an item
 */
public interface ItemComparator {
  /**
   * Compare the modifier of an item
   *
   * @param itemStack       the item
   * @param uuid            the unique id
   * @param stringReplacers the string replacers
   *
   * @return true if it matches, otherwise false
   */
  boolean compare(@NotNull ItemStack itemStack, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers);

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   * @param uuid      the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull ItemStack itemStack, @Nullable UUID uuid) {
    return compare(itemStack, uuid, Collections.emptyList());
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull ItemStack itemStack) {
    return compare(itemStack, null, Collections.emptyList());
  }
}
