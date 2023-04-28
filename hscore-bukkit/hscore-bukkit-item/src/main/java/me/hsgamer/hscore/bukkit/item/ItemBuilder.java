package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The item builder
 */
public class ItemBuilder {
  private final List<ItemModifier> itemModifiers = new ArrayList<>();
  private final List<StringReplacer> stringReplacers = new ArrayList<>();
  private ItemStack defaultItemStack;

  /**
   * Add an item modifier
   *
   * @param modifier the item modifier
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder addItemModifier(ItemModifier modifier) {
    itemModifiers.add(modifier);
    return this;
  }

  /**
   * Remove an item modifier
   *
   * @param modifier the item modifier
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder removeItemModifier(ItemModifier modifier) {
    itemModifiers.remove(modifier);
    return this;
  }

  /**
   * Get the map of item modifiers
   *
   * @return the item modifiers
   */
  public List<ItemModifier> getItemModifiers() {
    return Collections.unmodifiableList(itemModifiers);
  }

  /**
   * Get the list of string replacers
   *
   * @return the string replacers
   */
  public List<StringReplacer> getStringReplacers() {
    return Collections.unmodifiableList(stringReplacers);
  }

  /**
   * Add a string replacer
   *
   * @param replacer the string replacer
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder addStringReplacer(StringReplacer replacer) {
    this.stringReplacers.add(replacer);
    return this;
  }

  /**
   * Remove a string replacer
   *
   * @param replacer the string replacer
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder removeStringReplacer(StringReplacer replacer) {
    this.stringReplacers.remove(replacer);
    return this;
  }

  /**
   * Build the item
   *
   * @param uuid the unique id
   *
   * @return the item
   */
  public ItemStack build(@Nullable UUID uuid) {
    ItemStack itemStack = defaultItemStack == null ? new ItemStack(Material.STONE) : defaultItemStack.clone();
    for (ItemModifier modifier : itemModifiers) {
      itemStack = modifier.modify(itemStack, uuid, getStringReplacers());
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
  public ItemStack build(@NotNull Player player) {
    return build(player.getUniqueId());
  }

  /**
   * Build the item
   *
   * @return the item
   */
  public ItemStack build() {
    return build((UUID) null);
  }

  /**
   * Set the default item stack
   *
   * @param itemStack the item stack
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder setDefaultItemStack(ItemStack itemStack) {
    this.defaultItemStack = itemStack;
    return this;
  }

  /**
   * Set the default material
   *
   * @param material the material
   *
   * @return {@code this} for builder chain
   *
   * @see #setDefaultItemStack(ItemStack)
   */
  @Contract("_ -> this")
  public ItemBuilder setDefaultMaterial(Material material) {
    return setDefaultItemStack(new ItemStack(material));
  }
}
