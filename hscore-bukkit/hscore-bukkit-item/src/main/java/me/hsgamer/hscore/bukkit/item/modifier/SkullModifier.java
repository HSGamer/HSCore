package me.hsgamer.hscore.bukkit.item.modifier;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.bukkit.item.helper.VersionHelper;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier extends ItemMetaModifier {
  private String skullString = "";

  private static void setSkullValue(SkullMeta meta, String value) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", value));
    try {
      Field profileField = meta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(meta, profile);
    } catch (Exception e) {
      // IGNORE
    }
  }

  private static void setSkullURL(SkullMeta meta, String url) {
    setSkullValue(meta, Base64.getEncoder().encodeToString(
      String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()
    ));
  }

  private static void setSkull(SkullMeta meta, OfflinePlayer offlinePlayer) {
    if (offlinePlayer == null) {
      return;
    }
    if (VersionHelper.isAtLeast(12)) {
      meta.setOwningPlayer(offlinePlayer);
    } else {
      meta.setOwner(offlinePlayer.getName());
    }
  }

  private static void setSkull(SkullMeta meta, String skull) {
    if (Validate.isValidURL(skull)) {
      setSkullURL(meta, skull);
    } else if (skull.length() > 100 && Validate.isValidBase64(skull)) {
      setSkullValue(meta, skull);
    } else {
      OfflinePlayer player;
      if (Validate.isValidUUID(skull)) {
        player = Bukkit.getOfflinePlayer(UUID.fromString(skull));
      } else {
        player = Bukkit.getOfflinePlayer(skull);
      }
      setSkull(meta, player);
    }
  }

  private static SkullMeta getSkullMeta(String skull) {
    ItemStack itemStack;
    if (VersionHelper.isAtLeast(13)) {
      itemStack = new ItemStack(Material.valueOf("PLAYER_HEAD"));
    } else {
      itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"));
      itemStack.setDurability((short) 3);
    }
    SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
    setSkull(meta, skull);
    return meta;
  }

  private static String getSkullValue(SkullMeta meta) {
    GameProfile profile;
    try {
      Field profileField = meta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profile = (GameProfile) profileField.get(meta);
    } catch (Exception e) {
      return "";
    }

    Collection<Property> properties = profile.getProperties().get("textures");
    if (properties == null || properties.isEmpty()) {
      return "";
    }

    for (Property property : properties) {
      String value = property.getValue();
      if (!value.isEmpty()) {
        return value;
      }
    }
    return "";
  }

  private static String getSkullValue(String rawSkullValue) {
    return getSkullValue(getSkullMeta(rawSkullValue));
  }

  private String getFinalSkullString(UUID uuid, Collection<StringReplacer> replacers) {
    return StringReplacer.replace(skullString, uuid, replacers);
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    SkullMeta skullMeta = (SkullMeta) meta;
    setSkull(skullMeta, getFinalSkullString(uuid, stringReplacerMap.values()));
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
    return Objects.equals(
      getSkullValue(getFinalSkullString(uuid, stringReplacerMap.values())),
      getSkullValue((SkullMeta) meta)
    );
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
