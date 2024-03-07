package me.hsgamer.hscore.minecraft.item;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   * @param item           the item
   * @param uuid           the unique id
   * @param stringReplacer the string replacer
   *
   * @return true if it matches, otherwise false
   */
  boolean compare(@NotNull T item, @Nullable UUID uuid, StringReplacer stringReplacer);

  /**
   * Compare the modifier of an item
   *
   * @param item the item
   * @param uuid the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull T item, @Nullable UUID uuid) {
    return compare(item, uuid, StringReplacer.DUMMY);
  }

  /**
   * Compare the modifier of an item
   *
   * @param item the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compare(@NotNull T item) {
    return compare(item, null, StringReplacer.DUMMY);
  }
}
