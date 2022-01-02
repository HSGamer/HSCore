package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

/**
 * The name modifier
 */
public class NameModifier extends ItemMetaModifier {
  private String name;

  @Override
  public String getName() {
    return "name";
  }

  /**
   * Set the name
   *
   * @param name the name
   *
   * @return {@code this} for builder chain
   */
  public NameModifier setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    meta.setDisplayName(StringReplacer.replace(name, uuid, stringReplacerMap.values()));
    return meta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    this.name = meta.getDisplayName();
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return meta.hasDisplayName();
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    String replaced = StringReplacer.replace(this.name, uuid, stringReplacerMap.values());
    return (!meta.hasDisplayName() && replaced == null) || replaced.equals(meta.getDisplayName());
  }

  @Override
  public Object toObject() {
    return this.name;
  }

  @Override
  public void loadFromObject(Object object) {
    this.name = String.valueOf(object);
  }
}
