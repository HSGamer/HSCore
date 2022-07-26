package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier extends ItemMetaModifier {
  private String skullString = "";

  private String getFinalSkull(UUID uuid, Collection<StringReplacer> replacers) {
    return StringReplacer.replace(skullString, uuid, replacers);
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    SkullMeta skullMeta = (SkullMeta) meta;
    skullMeta.setOwner(getFinalSkull(uuid, stringReplacerMap.values()));
    return skullMeta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    if (meta instanceof SkullMeta) {
      SkullMeta skullMeta = (SkullMeta) meta;
      skullString = skullMeta.getOwner();
    }
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return meta instanceof SkullMeta;
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return meta instanceof SkullMeta && ((SkullMeta) meta).getOwner().equals(getFinalSkull(uuid, stringReplacerMap.values()));
  }

  @Override
  public String getName() {
    return "skull";
  }

  @Override
  public Object toObject() {
    return skullString;
  }

  @Override
  public void loadFromObject(Object object) {
    this.skullString = String.valueOf(object);
  }

  /**
   * Set the skull
   *
   * @param skull the skull
   *
   * @return {@code this} for builder chain
   */
  public SkullModifier setSkull(String skull) {
    this.skullString = skull;
    return this;
  }
}
