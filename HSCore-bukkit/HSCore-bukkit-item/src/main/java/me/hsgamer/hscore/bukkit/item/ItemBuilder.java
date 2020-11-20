package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * The item builder
 */
public class ItemBuilder {
  private final List<ItemModifier> itemModifiers = new LinkedList<>();
  private StringReplacer stringReplacer = StringReplacer.DUMMY;

  /**
   * Add an item modifier
   *
   * @param modifier the item modifier
   */
  public ItemBuilder addItemModifier(ItemModifier modifier) {
    itemModifiers.add(modifier);
    return this;
  }

  /**
   * Get the list of item modifiers
   *
   * @return the item modifiers
   */
  public List<ItemModifier> getItemModifiers() {
    return itemModifiers;
  }

  /**
   * Get the string replacer
   *
   * @return the string replacer
   */
  public StringReplacer getStringReplacer() {
    return stringReplacer;
  }

  /**
   * Set the string replacer
   *
   * @param replacer the string replacer
   */
  public ItemBuilder setStringReplacer(StringReplacer replacer) {
    this.stringReplacer = replacer;
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
    ItemStack itemStack = null;
    for (ItemModifier modifier : itemModifiers) {
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
