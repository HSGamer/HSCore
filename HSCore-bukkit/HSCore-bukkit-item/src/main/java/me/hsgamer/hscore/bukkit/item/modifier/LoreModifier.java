package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The lore modifier
 */
public class LoreModifier implements ItemModifier {
  private final List<String> lore = new ArrayList<>();

  @Override
  public String getName() {
    return "lore";
  }

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = original.getItemMeta();
    itemMeta.setLore(lore.stream().map(s -> StringReplacer.replace(s, uuid, stringReplacerMap.values())).collect(Collectors.toList()));
    original.setItemMeta(itemMeta);
    return original;
  }

  @Override
  public Object toObject() {
    return lore;
  }

  @Override
  public void loadFromObject(Object object) {
    setLore(CollectionUtils.createStringListFromObject(object, false));
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    setLore(itemStack.getItemMeta().getLore());
  }

  @Override
  public boolean canLoadFromItemStack(ItemStack itemStack) {
    return itemStack.getItemMeta().hasLore();
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    return (!itemMeta.hasLore() && this.lore.isEmpty())
      || this.lore.stream().map(s -> StringReplacer.replace(s, uuid, stringReplacerMap.values())).collect(Collectors.toList()).equals(itemMeta.getLore());
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  public LoreModifier setLore(String... lore) {
    return setLore(Arrays.asList(lore));
  }

  /**
   * Add a lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  public LoreModifier addLore(String lore) {
    this.lore.add(lore);
    return this;
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  public LoreModifier setLore(Collection<String> lore) {
    clearLore();
    this.lore.addAll(lore);
    return this;
  }

  /**
   * Clear the lore
   *
   * @return {@code this} for builder chain
   */
  public LoreModifier clearLore() {
    this.lore.clear();
    return this;
  }
}
