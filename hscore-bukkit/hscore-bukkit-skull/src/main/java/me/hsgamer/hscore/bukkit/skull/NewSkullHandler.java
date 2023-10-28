package me.hsgamer.hscore.bukkit.skull;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

class NewSkullHandler implements SkullHandler {
  private final Map<URL, PlayerProfile> profileMap = new HashMap<>();

  @Override
  public void setSkullByPlayer(SkullMeta meta, OfflinePlayer player) {
    meta.setOwningPlayer(player);
  }

  @Override
  public void setSkullByURL(SkullMeta meta, URL url) {
    PlayerProfile profile = profileMap.computeIfAbsent(url, url1 -> {
      PlayerProfile newProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
      PlayerTextures textures = newProfile.getTextures();
      textures.setSkin(url1);
      return newProfile;
    });
    meta.setOwnerProfile(profile);
  }

  @Override
  public String getSkullValue(SkullMeta meta) {
    PlayerProfile profile = meta.getOwnerProfile();
    if (profile == null) {
      return "";
    }

    PlayerTextures textures = profile.getTextures();
    URL url = textures.getSkin();
    if (url == null) {
      return "";
    }

    return url.toString();
  }

  @Override
  public boolean compareSkull(SkullMeta meta1, SkullMeta meta2) {
    PlayerProfile profile1 = meta1.getOwnerProfile();
    PlayerProfile profile2 = meta2.getOwnerProfile();
    if (profile1 == null || profile2 == null) {
      return true;
    }

    URL url1 = profile1.getTextures().getSkin();
    URL url2 = profile2.getTextures().getSkin();

    return Objects.equals(url1, url2);
  }
}
