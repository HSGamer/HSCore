package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

/**
 * The name modifier
 */
public class NameModifier implements ItemModifier {
  private String name;

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = original.getItemMeta();
    itemMeta.setDisplayName(StringReplacer.replace(name, uuid, stringReplacerMap.values()));
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

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.name = itemStack.getItemMeta().getDisplayName();
  }

  @Override
  public boolean canLoadFromItemStack(ItemStack itemStack) {
    return itemStack.getItemMeta().hasDisplayName();
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    String replaced = StringReplacer.replace(this.name, uuid, stringReplacerMap.values());
    return (!itemMeta.hasDisplayName() && Validate.isNullOrEmpty(replaced)) || replaced.equals(itemMeta.getDisplayName());
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
