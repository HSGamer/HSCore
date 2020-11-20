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
  public ItemStack modify(ItemStack original, UUID uuid, StringReplacer replacer) {
    ItemMeta itemMeta = original.getItemMeta();
    itemMeta.setLore(lore.stream().map(s -> replacer.replace(s, uuid)).collect(Collectors.toList()));
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
