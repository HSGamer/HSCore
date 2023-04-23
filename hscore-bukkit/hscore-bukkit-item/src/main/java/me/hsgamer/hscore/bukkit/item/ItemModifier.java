package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The item modifier
 */
public interface ItemModifier {
  /**
   * Get the name of the modifier
   *
   * @return the name
   */
  String getName();

  /**
   * Modify the item
   *
   * @param original          the original item
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return the modified item
   */
  @NotNull
  ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap);

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
   * @param itemStack the item
   *
   * @return true if it can
   */
  default boolean loadFromItemStack(ItemStack itemStack) {
    return false;
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack         the item
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return true if it matches, otherwise false
   */
  boolean compareWithItemStack(@NotNull ItemStack itemStack, @Nullable UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap);

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   * @param uuid      the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(@NotNull ItemStack itemStack, @Nullable UUID uuid) {
    return compareWithItemStack(itemStack, uuid, Collections.emptyMap());
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(@NotNull ItemStack itemStack) {
    return compareWithItemStack(itemStack, null, Collections.emptyMap());
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
  default ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid) {
    return modify(original, uuid, Collections.emptyMap());
  }

  /**
   * Modify the item
   *
   * @param original the original item
   *
   * @return the modified item
   */
  @NotNull
  default ItemStack modify(@NotNull ItemStack original) {
    return modify(original, null);
  }
}
