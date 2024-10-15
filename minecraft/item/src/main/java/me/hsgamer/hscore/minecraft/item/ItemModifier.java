package me.hsgamer.hscore.minecraft.item;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The item modifier
 *
 * @param <T> the type of the item
 */
public interface ItemModifier<T> {
  /**
   * Modify the item
   *
   * @param original       the original item
   * @param uuid           the unique id
   * @param stringReplacer the string replacer
   *
   * @return the modified item
   */
  @NotNull
  T modify(@NotNull T original, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer);

  /**
   * Serialize the modifier to an object
   *
   * @return the object
   */
  Object toObject();

  /**
   * Load the modifier from an object
   *
   * @param object the object
   */
  void loadFromObject(Object object);

  /**
   * Load the modifier from an item
   *
   * @param item the item
   *
   * @return true if it can
   */
  default boolean loadFromItem(T item) {
    return false;
  }

  /**
   * Modify the item
   *
   * @param original the original item
   * @param uuid     the unique id
   *
   * @return the modified item
   */
  @NotNull
  default T modify(@NotNull T original, @Nullable UUID uuid) {
    return modify(original, uuid, StringReplacer.DUMMY);
  }

  /**
   * Modify the item
   *
   * @param original the original item
   *
   * @return the modified item
   */
  @NotNull
  default T modify(@NotNull T original) {
    return modify(original, null);
  }
}
