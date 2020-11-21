package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import me.hsgamer.hscore.map.CaseInsensitiveStringLinkedMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The item builder
 */
public class ItemBuilder {
  private final Map<String, ItemModifier> itemModifierMap = new CaseInsensitiveStringLinkedMap<>();
  private final Map<String, StringReplacer> stringReplacerMap = new CaseInsensitiveStringLinkedMap<>();

  /**
   * Add an item modifier
   *
   * @param name     the name of the modifier
   * @param modifier the item modifier
   */
  public ItemBuilder addItemModifier(String name, ItemModifier modifier) {
    itemModifierMap.put(name, modifier);
    return this;
  }

  /**
   * Remove an item modifier
   *
   * @param name the name of the modifier
   */
  public ItemBuilder removeItemModifier(String name) {
    itemModifierMap.remove(name);
    return this;
  }

  /**
   * Get the map of item modifiers
   *
   * @return the item modifiers
   */
  public Map<String, ItemModifier> getItemModifierMap() {
    return Collections.unmodifiableMap(itemModifierMap);
  }

  /**
   * Get the map of the string replacer
   *
   * @return the string replacers
   */
  public Map<String, StringReplacer> getStringReplacerMap() {
    return Collections.unmodifiableMap(stringReplacerMap);
  }

  /**
   * Add a string replacer
   *
   * @param name     the name of the string replacer
   * @param replacer the string replacer
   */
  public ItemBuilder addStringReplacer(String name, StringReplacer replacer) {
    this.stringReplacerMap.put(name, replacer);
    return this;
  }

  /**
   * Remove a string replacer
   *
   * @param name the name of the string replacer
   */
  public ItemBuilder removeStringReplacer(String name) {
    this.stringReplacerMap.remove(name);
    return this;
  }

  /**
   * Build the item
   *
   * @param uuid the unique id
   *
   * @return the item
   */
  public ItemStack build(UUID uuid) {
    ItemStack itemStack = new ItemStack(Material.AIR);
    for (ItemModifier modifier : itemModifierMap.values()) {
      itemStack = modifier.modify(itemStack, uuid, this);
    }
    return itemStack;
  }

  /**
   * Build the item
   *
   * @param player the player
   *
   * @return the item
   */
  public ItemStack build(Player player) {
    return build(player.getUniqueId());
  }

  /**
   * Build the item
   *
   * @return the item
   */
  public ItemStack build() {
    return build(UUID.randomUUID());
  }
}
