package me.hsgamer.hscore.bukkit.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SkullUtils {
  private static final Map<String, CompletableFuture<OfflinePlayer>> offlinePlayerMap = new ConcurrentHashMap<>();

  private SkullUtils() {

  }

  public static ItemMeta parseSkull(ItemMeta itemMeta, String skullOwner) {
    if (itemMeta instanceof SkullMeta) {
      if (isValidURL(skullOwner)) {
        setSkullWithURL((SkullMeta) itemMeta, skullOwner);
      } else {
        CompletableFuture<OfflinePlayer> completableFuture = offlinePlayerMap.computeIfAbsent(skullOwner, s -> CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(s)));
        if (completableFuture.isDone()) {
          ((SkullMeta) itemMeta).setOwner(completableFuture.join().getName());
        }
      }
    }
    return itemMeta;
  }

  public static void setSkullWithURL(SkullMeta skullMeta, String url) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
    try {
      Field profileField;
      profileField = skullMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(skullMeta, profile);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      Bukkit.getLogger().log(Level.WARNING, "Unexpected error when getting skull", e);
    }
  }

  private static boolean isValidURL(String string) {
    try {
      new URL(string).toURI();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
