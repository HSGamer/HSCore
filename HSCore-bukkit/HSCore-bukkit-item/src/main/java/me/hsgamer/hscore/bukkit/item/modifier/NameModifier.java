package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * The name modifier
 */
public class NameModifier implements ItemModifier {
  private String name;

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, StringReplacer replacer) {
    ItemMeta itemMeta = original.getItemMeta();
    itemMeta.setDisplayName(replacer.replace(name, uuid));
    original.setItemMeta(itemMeta);
    return original;
  }

  @Override
  public Object toObject() {
    return this.name;
  }

  @Override
  public void loadFromObject(Object object) {
    this.name = String.valueOf(object);
  }

  /**
   * Set the name
   *
   * @param name the name
   */
  public NameModifier setName(String name) {
    this.name = name;
    return this;
  }
}
