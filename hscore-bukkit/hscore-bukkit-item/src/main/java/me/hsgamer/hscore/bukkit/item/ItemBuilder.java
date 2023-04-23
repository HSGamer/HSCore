package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.collections.map.CaseInsensitiveStringLinkedMap;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The item builder
 */
public class ItemBuilder {
  private final List<ItemModifier> itemModifiers = new LinkedList<>();
  private final Map<String, StringReplacer> stringReplacerMap = new CaseInsensitiveStringLinkedMap<>();
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
   * @param name the name of the modifier
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder removeItemModifier(String name) {
    itemModifiers.removeIf(itemModifier -> itemModifier.getName().equals(name));
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
   * Serialize the item modifiers
   *
   * @return the object map
   */
  public Map<String, Object> serializeItemModifiers() {
    Map<String, Object> map = new HashMap<>();
    itemModifiers.forEach(itemModifier -> map.put(itemModifier.getName(), itemModifier.toObject()));
    return map;
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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_, _ -> this")
  public ItemBuilder addStringReplacer(String name, StringReplacer replacer) {
    this.stringReplacerMap.put(name, replacer);
    return this;
  }

  /**
   * Remove a string replacer
   *
   * @param name the name of the string replacer
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
  public ItemStack build(@Nullable UUID uuid) {
    ItemStack itemStack = defaultItemStack == null ? new ItemStack(Material.STONE) : defaultItemStack.clone();
    for (ItemModifier modifier : itemModifiers) {
      itemStack = modifier.modify(itemStack, uuid, getStringReplacerMap());
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
