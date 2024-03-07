package me.hsgamer.hscore.bukkit.skull;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.URL;
import java.util.UUID;

/**
 * The skull handler
 */
public interface SkullHandler {
  /**
   * Get the instance
   *
   * @return the instance
   */
  static SkullHandler getInstance() {
    try {
      Class.forName("org.bukkit.profile.PlayerProfile");
      return new NewSkullHandler();
    } catch (ClassNotFoundException e) {
      return new OldSkullHandler();
    }
  }

  /**
   * Set the skull by the name
   *
   * @param meta the skull meta
   * @param name the name
   */
  @SuppressWarnings("deprecation")
  default void setSkullByName(SkullMeta meta, String name) {
    setSkullByPlayer(meta, Bukkit.getOfflinePlayer(name));
  }

  /**
   * Set the skull by the UUID
   *
   * @param meta the skull meta
   * @param uuid the UUID
   */
  default void setSkullByUUID(SkullMeta meta, UUID uuid) {
    setSkullByPlayer(meta, Bukkit.getOfflinePlayer(uuid));
  }

  /**
   * Set the skull by the player
   *
   * @param meta   the skull meta
   * @param player the player
   */
  void setSkullByPlayer(SkullMeta meta, OfflinePlayer player);

  /**
   * Set the skull by the URL
   *
   * @param meta the skull meta
   * @param url  the URL
   */
  void setSkullByURL(SkullMeta meta, URL url);

  /**
   * Set the skull by the URL
   *
   * @param meta the skull meta
   * @param url  the URL
   */
  default void setSkullByURL(SkullMeta meta, String url) {
    try {
      setSkullByURL(meta, new URL(url));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the skull value
   *
   * @param meta the skull meta
   *
   * @return the skull value
   */
  String getSkullValue(SkullMeta meta);

  /**
   * Compare 2 skull metas
   *
   * @param meta1 the first skull meta
   * @param meta2 the second skull meta
   *
   * @return true if they are the same
   */
  boolean compareSkull(SkullMeta meta1, SkullMeta meta2);
}
