package me.hsgamer.hscore.minecraft.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * An extension to {@link ItemModifier} to compare the modifier of an item
 *
 * @param <T> the type of the item
 */
public interface ItemComparator<T> {
  /**
   * Compare the modifier of an item
   *
   * @param item            the item
   * @param uuid            the unique id
   * @param stringReplacers the string replacers
   *
   * @return true if it matches, otherwise false
   */
  boolean compare(@NotNull T item, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers);

  /**
   * Compare the modifier of an item
   *
   * @param item the item
   * @param uuid the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull T item, @Nullable UUID uuid) {
    return compare(item, uuid, Collections.emptyList());
  }

  /**
   * Compare the modifier of an item
   *
   * @param item the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull T item) {
    return compare(item, null, Collections.emptyList());
  }
}
