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
   */
  public abstract void modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap);

  /**
   * Load the modifier from the item meta
   *
   * @param meta the item meta
   *
   * @see #loadFromItemStack(ItemStack)
   */
  public abstract void loadFromItemMeta(ItemMeta meta);

  /**
   * Check if a modifier can be loaded from the item meta
   *
   * @param meta the item meta
   *
   * @return true if it can
   *
   * @see #canLoadFromItemStack(ItemStack)
   */
  public abstract boolean canLoadFromItemMeta(ItemMeta meta);

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
    if (original.hasItemMeta()) {
      ItemMeta itemMeta = original.getItemMeta();
      this.modifyMeta(itemMeta, uuid, stringReplacerMap);
      original.setItemMeta(itemMeta);
    }
    return original;
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    if (itemStack.hasItemMeta()) {
      this.loadFromItemMeta(itemStack.getItemMeta());
    }
  }

  @Override
  public boolean canLoadFromItemStack(ItemStack itemStack) {
    return itemStack.hasItemMeta() && this.canLoadFromItemMeta(itemStack.getItemMeta());
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return itemStack.hasItemMeta() && this.compareWithItemMeta(itemStack.getItemMeta(), uuid, stringReplacerMap);
  }
}
