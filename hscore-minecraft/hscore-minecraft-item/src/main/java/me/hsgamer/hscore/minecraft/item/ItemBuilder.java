package me.hsgamer.hscore.minecraft.item;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The item builder
 *
 * @param <T> the type of the item
 */
public abstract class ItemBuilder<T> {
  private final List<ItemModifier<T>> itemModifiers = new ArrayList<>();
  private final List<StringReplacer> stringReplacers = new ArrayList<>();

  /**
   * Get the default item
   *
   * @return the default item
   */
  @NotNull
  protected abstract T getDefaultItem();

  /**
   * Add an item modifier
   *
   * @param modifier the item modifier
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public ItemBuilder<T> addItemModifier(ItemModifier<T> modifier) {
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
  public ItemBuilder<T> removeItemModifier(ItemModifier<T> modifier) {
    itemModifiers.remove(modifier);
    return this;
  }

  /**
   * Get the map of item modifiers
   *
   * @return the item modifiers
   */
  public List<ItemModifier<T>> getItemModifiers() {
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
  public ItemBuilder<T> addStringReplacer(StringReplacer replacer) {
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
  public ItemBuilder<T> removeStringReplacer(StringReplacer replacer) {
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
  public T build(@Nullable UUID uuid) {
    T item = getDefaultItem();
    for (ItemModifier<T> modifier : itemModifiers) {
      item = modifier.modify(item, uuid, getStringReplacers());
    }
    return item;
  }

  /**
   * Build the item
   *
   * @return the item
   */
  public T build() {
    return build(null);
  }
}
