package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * An extension to {@link ItemModifier} to compare the modifier of an item
 */
public interface ItemComparator {
  /**
   * Compare the modifier of an item
   *
   * @param itemStack         the item
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return true if it matches, otherwise false
   */
  boolean compare(@NotNull ItemStack itemStack, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap);

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   * @param uuid      the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull ItemStack itemStack, @Nullable UUID uuid) {
    return compare(itemStack, uuid, Collections.emptyMap());
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull ItemStack itemStack) {
    return compare(itemStack, null, Collections.emptyMap());
  }
}
