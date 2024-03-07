package me.hsgamer.hscore.bukkit.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

class OldSkullHandler implements SkullHandler {
  private final Method getProfileMethod;

  OldSkullHandler() {
    Method method = null;
    try {
      //noinspection JavaReflectionMemberAccess
      method = Property.class.getDeclaredMethod("value");
    } catch (Exception e) {
      try {
        //noinspection JavaReflectionMemberAccess
        method = Property.class.getDeclaredMethod("getValue");
      } catch (NoSuchMethodException ex) {
        // IGNORE
      }
    }
    getProfileMethod = method;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void setSkullByPlayer(SkullMeta meta, OfflinePlayer player) {
    if (VersionUtils.isAtLeast(12)) {
      meta.setOwningPlayer(player);
    } else {
      meta.setOwner(player.getName());
    }
  }

  @Override
  public void setSkullByURL(SkullMeta meta, URL url) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes())));

    try {
      Method setProfile = meta.getClass().getMethod("setProfile", GameProfile.class);
      setProfile.setAccessible(true);
      setProfile.invoke(meta, profile);
    } catch (Exception e) {
      try {
        Field profileField = meta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(meta, profile);
      } catch (Exception ignored) {
        // IGNORE
      }
    }
  }

  @Override
  public String getSkullValue(SkullMeta meta) {
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
      String value;
      try {
        value = (String) getProfileMethod.invoke(property);
      } catch (Exception e) {
        continue;
      }

      if (!value.isEmpty()) {
        return value;
      }
    }
    return "";
  }

  @Override
  public boolean compareSkull(SkullMeta meta1, SkullMeta meta2) {
    return getSkullValue(meta1).equals(getSkullValue(meta2));
  }
}
