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
