package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

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
  ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap);

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
  boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap);

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   * @param uuid      the unique id
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(ItemStack itemStack, UUID uuid) {
    return compareWithItemStack(itemStack, uuid, Collections.emptyMap());
  }

  /**
   * Compare the modifier of an item
   *
   * @param itemStack the item
   *
   * @return true if it matches, otherwise false
   */
  default boolean compareWithItemStack(ItemStack itemStack) {
    return compareWithItemStack(itemStack, UUID.randomUUID(), Collections.emptyMap());
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
    return modify(original, uuid, Collections.emptyMap());
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
    return modify(original, uuid, itemBuilder.getStringReplacerMap());
  }
}
