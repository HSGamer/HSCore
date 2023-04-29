package me.hsgamer.hscore.bukkit.item.modifier;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier implements ItemMetaModifier, ItemMetaComparator {
  private static final SkullMeta delegateSkullMeta;

  static {
    ItemStack itemStack;
    if (VersionUtils.isAtLeast(13)) {
      itemStack = new ItemStack(Material.valueOf("PLAYER_HEAD"));
    } else {
      itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"));
      itemStack.setDurability((short) 3);
    }
    delegateSkullMeta = (SkullMeta) itemStack.getItemMeta();
  }

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
    if (VersionUtils.isAtLeast(12)) {
      meta.setOwningPlayer(offlinePlayer);
    } else {
      meta.setOwner(offlinePlayer.getName());
    }
  }

  private static void setSkull(SkullMeta meta, String skull) {
    if (BukkitUtils.isUsername(skull)) {
      setSkull(meta, Bukkit.getOfflinePlayer(skull));
    } else if (Validate.isValidUUID(skull)) {
      setSkull(meta, Bukkit.getOfflinePlayer(UUID.fromString(skull)));
    } else if (Validate.isValidURL(skull)) {
      setSkullURL(meta, skull);
    } else if (skull.length() > 100 && Validate.isValidBase64(skull)) {
      setSkullValue(meta, skull);
    } else {
      setSkullURL(meta, "https://textures.minecraft.net/texture/" + skull);
    }
  }

  private static SkullMeta getSkullMeta(String skull) {
    SkullMeta meta = delegateSkullMeta.clone();
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
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    setSkull((SkullMeta) meta, getFinalSkullString(uuid, stringReplacers));
    return meta;
  }

  @Override
  public boolean loadFromItemMeta(ItemMeta meta) {
    if (meta instanceof SkullMeta) {
      skullString = getSkullValue((SkullMeta) meta);
      return true;
    }
    return false;
  }

  @Override
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (!(meta instanceof SkullMeta)) {
      return false;
    }
    return Objects.equals(
      getSkullValue(getFinalSkullString(uuid, stringReplacers)),
      getSkullValue((SkullMeta) meta)
    );
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
