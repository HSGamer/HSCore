package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.CommonUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The lore modifier
 */
public class LoreModifier implements ItemModifier {
  private List<String> lore = new ArrayList<>();

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, List<StringReplacer> stringReplacers) {
    ItemMeta itemMeta = original.getItemMeta();
    itemMeta.setLore(lore.stream().map(s -> StringReplacer.replace(s, uuid, stringReplacers)).collect(Collectors.toList()));
    original.setItemMeta(itemMeta);
    return original;
  }

  @Override
  public Object toObject() {
    return lore;
  }

  @Override
  public void loadFromObject(Object object) {
    this.lore = CommonUtils.createStringListFromObject(object, false);
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.lore.clear();
    this.lore.addAll(itemStack.getItemMeta().getLore());
  }

  @Override
  public boolean canLoadFromItemStack(ItemStack itemStack) {
    return itemStack.getItemMeta().hasLore();
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, List<StringReplacer> stringReplacers) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    return (!itemMeta.hasLore() && this.lore.isEmpty())
      || this.lore.stream().map(s -> StringReplacer.replace(s, uuid, stringReplacers)).collect(Collectors.toList()).equals(itemMeta.getLore());
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   */
  public LoreModifier setLore(String... lore) {
    this.lore.clear();
    this.lore = Arrays.asList(lore);
    return this;
  }

  /**
   * Add a lore
   *
   * @param lore the lore
   */
  public LoreModifier addLore(String lore) {
    this.lore.add(lore);
    return this;
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   */
  public LoreModifier setLore(Collection<String> lore) {
    this.lore.clear();
    this.lore.addAll(lore);
    return this;
  }

  /**
   * Clear the lore
   */
  public LoreModifier clearLore() {
    this.lore.clear();
    return this;
  }
}
