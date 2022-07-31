package me.hsgamer.hscore.bukkit.item.modifier;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier extends ItemMetaModifier {
  private static final LoadingCache<String, String> skullCache;
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
    skullCache = CacheBuilder.newBuilder()
      .expireAfterAccess(10, TimeUnit.MINUTES)
      .build(new CacheLoader<String, String>() {
        @Override
        public String load(@NotNull String skull) {
          return getSkullValue(skull);
        }
      });
  }

  private String skullString = "";
  private boolean useCache = true;

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

  public static void setCachedSkull(SkullMeta meta, String skull) {
    try {
      String cachedSkull = skullCache.get(skull);
      if (cachedSkull != null) {
        setSkullValue(meta, cachedSkull);
      }
    } catch (Exception e) {
      // IGNORED
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
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    SkullMeta skullMeta = (SkullMeta) meta;
    String finalSkullString = getFinalSkullString(uuid, stringReplacerMap.values());
    if (useCache) {
      setCachedSkull(skullMeta, finalSkullString);
    } else {
      setSkull(skullMeta, finalSkullString);
    }
    return skullMeta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    if (meta instanceof SkullMeta) {
      SkullMeta skullMeta = (SkullMeta) meta;
      skullString = getSkullValue(skullMeta);
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

  /**
   * Set whether to use the cache
   *
   * @param useCache whether to use the cache
   *
   * @return {@code this} for builder chain
   */
  public SkullModifier setUseCache(boolean useCache) {
    this.useCache = useCache;
    return this;
  }
}
