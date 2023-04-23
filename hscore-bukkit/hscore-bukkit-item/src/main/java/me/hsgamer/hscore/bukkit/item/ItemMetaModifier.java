package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

/**
 * The {@link ItemMeta} modifier
 */
public abstract class ItemMetaModifier implements ItemModifier {
  /**
   * Modify the item meta
   *
   * @param meta              the item meta
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return the modified item meta
   */
  public abstract ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap);

  /**
   * Load the modifier from the item meta
   *
   * @param meta the item meta
   *
   * @see #loadFromItemStack(ItemStack)
   */
  public abstract boolean loadFromItemMeta(ItemMeta meta);

  /**
   * Compare the modifier of an item meta
   *
   * @param meta              the item meta
   * @param uuid              the unique id
   * @param stringReplacerMap the map of string replacers
   *
   * @return true if it matches, otherwise false
   *
   * @see #compareWithItemStack(ItemStack, UUID, Map)
   */
  public abstract boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap);

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = original.getItemMeta();
    if (itemMeta != null) {
      original.setItemMeta(this.modifyMeta(itemMeta, uuid, stringReplacerMap));
    }
    return original;
  }

  @Override
  public boolean loadFromItemStack(ItemStack itemStack) {
    if (itemStack.hasItemMeta()) {
      return this.loadFromItemMeta(itemStack.getItemMeta());
    }
    return false;
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return itemStack.hasItemMeta() && this.compareWithItemMeta(itemStack.getItemMeta(), uuid, stringReplacerMap);
  }
}
