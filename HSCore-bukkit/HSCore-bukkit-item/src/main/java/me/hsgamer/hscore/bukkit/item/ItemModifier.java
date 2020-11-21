package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The item modifier
 */
public interface ItemModifier {
  /**
   * Modify the item
   *
   * @param original        the original item
   * @param uuid            the unique id
   * @param stringReplacers the list of string replacers
   *
   * @return the modified item
   */
  ItemStack modify(ItemStack original, UUID uuid, List<StringReplacer> stringReplacers);

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
   * Load the modifier from an item <br> Should call {@link #canLoadFromItemStack(ItemStack)} before using this method
   *
   * @param itemStack the item
   */
  void loadFromItemStack(ItemStack itemStack);

  /**
   * Check if a modifier can be loaded from the item
   *
   * @param itemStack the item
   *
   * @return true if it can
   */
  default boolean canLoadFromItemStack(ItemStack itemStack) {
    return true;
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack       the item
   * @param uuid            the unique id
   * @param stringReplacers the list of string replacers
   *
   * @return true if it matches, otherwise false
   */
  boolean compareWithItemStack(ItemStack itemStack, UUID uuid, List<StringReplacer> stringReplacers);

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   * @param uuid      the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(ItemStack itemStack, UUID uuid) {
    return compareWithItemStack(itemStack, uuid, Collections.emptyList());
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(ItemStack itemStack) {
    return compareWithItemStack(itemStack, UUID.randomUUID(), Collections.emptyList());
  }

  /**
   * Modify the item
   *
   * @param original the original item
   * @param uuid     the unique id
   *
   * @return the modified item
   */
  default ItemStack modify(ItemStack original, UUID uuid) {
    return modify(original, uuid, Collections.emptyList());
  }

  /**
   * Modify the item
   *
   * @param original    the original item
   * @param uuid        the unique id
   * @param itemBuilder the item builder
   *
   * @return the modified item
   */
  default ItemStack modify(ItemStack original, UUID uuid, ItemBuilder itemBuilder) {
    return modify(original, uuid, itemBuilder.getStringReplacers());
  }
}
