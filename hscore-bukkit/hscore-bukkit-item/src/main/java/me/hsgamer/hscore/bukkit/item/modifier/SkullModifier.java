package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.bukkit.item.helper.VersionHelper;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier extends ItemMetaModifier {
  private String skullString = "";

  private static void setSkull(SkullMeta meta, String skull) {
    if (Validate.isValidUUID(skull)) {
      OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(skull));
      if (VersionHelper.isAtLeast(12)) {
        meta.setOwningPlayer(player);
      } else {
        meta.setOwner(player.getName());
      }
    } else {
      meta.setOwner(skull);
    }
  }

  private static String getSkullValue(SkullMeta meta, boolean isUUID) {
    if (isUUID) {
      if (VersionHelper.isAtLeast(12)) {
        OfflinePlayer player = meta.getOwningPlayer();
        return player == null ? null : player.getUniqueId().toString();
      } else {
        return meta.getOwner();
      }
    } else {
      return meta.getOwner();
    }
  }

  private String getFinalSkull(UUID uuid, Collection<StringReplacer> replacers) {
    return StringReplacer.replace(skullString, uuid, replacers);
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    SkullMeta skullMeta = (SkullMeta) meta;
    setSkull(skullMeta, getFinalSkull(uuid, stringReplacerMap.values()));
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
    if (!(meta instanceof SkullMeta)) {
      return false;
    }
    SkullMeta skullMeta = (SkullMeta) meta;
    String skullValue = getFinalSkull(uuid, stringReplacerMap.values());
    return Objects.equals(getSkullValue(skullMeta, Validate.isValidUUID(skullValue)), skullValue);
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
