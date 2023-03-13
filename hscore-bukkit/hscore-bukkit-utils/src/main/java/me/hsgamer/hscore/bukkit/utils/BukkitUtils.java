package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Methods on Bukkit
 */
public final class BukkitUtils {
  private BukkitUtils() {
    // EMPTY
  }

  /**
   * Get ping
   *
   * @param player the player
   *
   * @return the ping of the player
   */
  public static int getPing(@NotNull final Player player) {
    Object entityPlayer;
    int ping = -9;
    try {
      entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
    } catch (Exception e) {
      Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting handle", e);
      return ping;
    }
    try {
      ping = entityPlayer.getClass().getField("ping").getInt(entityPlayer);
    } catch (Exception e) {
      try {
        ping = entityPlayer.getClass().getField("e").getInt(entityPlayer);
      } catch (Exception e1) {
        Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting ping", e1);
      }
    }
    return ping;
  }

  /**
   * Get all unique ids
   *
   * @return the unique ids
   */
  @NotNull
  public static List<UUID> getAllUniqueIds() {
    return Arrays.stream(Bukkit.getOfflinePlayers())
      .parallel()
      .map(OfflinePlayer::getUniqueId)
      .collect(Collectors.toList());
  }

  /**
   * Get all player names
   *
   * @return the player names
   */
  @NotNull
  public static List<String> getAllPlayerNames() {
    return Arrays.stream(Bukkit.getOfflinePlayers())
      .parallel()
      .map(OfflinePlayer::getName)
      .collect(Collectors.toList());
  }

  /**
   * Get missing plugins from a list of given plugins
   *
   * @param depends the list of plugins
   *
   * @return the missing plugins
   */
  @NotNull
  public static List<String> getMissingDepends(@NotNull final List<String> depends) {
    return depends.stream()
      .filter(depend -> Bukkit.getPluginManager().getPlugin(depend) == null)
      .collect(Collectors.toList());
  }

  /**
   * Check if the string is the username
   *
   * @param string            the input string
   * @param allowedCharacters the allowed characters
   *
   * @return true if it is
   *
   * @see <a href="https://github.com/CryptoMorin/XSeries/blob/30544507ad1dd1322936caa41cc1fc28c80c4565/src/main/java/com/cryptomorin/xseries/SkullUtils.java#L301-L317">XSeries's Code</a>
   */
  public static boolean isUsername(@NotNull String string, List<Character> allowedCharacters) {
    int len = string.length();
    if (len > 16) return false;

    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      if (!Character.isLetterOrDigit(ch) && !allowedCharacters.contains(ch)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the string is the username
   *
   * @param string the input string
   *
   * @return true if it is
   *
   * @see #isUsername(String, List)
   */
  public static boolean isUsername(@NotNull String string) {
    return isUsername(string, Arrays.asList('_', '-', '.'));
  }

  /**
   * Center the location
   *
   * @param location the location
   * @param centerX  should the x be centered?
   * @param centerY  should the y be centered?
   * @param centerZ  should the z be centered?
   *
   * @return the centered location
   */
  public static Location centerLocation(Location location, boolean centerX, boolean centerY, boolean centerZ) {
    return new Location(
      location.getWorld(),
      location.getBlockX() + (centerX ? 0.5 : 0),
      location.getBlockY() + (centerY ? 0.5 : 0),
      location.getBlockZ() + (centerZ ? 0.5 : 0),
      location.getYaw(),
      location.getPitch()
    );
  }

  /**
   * Center the location
   *
   * @param location the location
   *
   * @return the centered location
   *
   * @see BukkitUtils#centerLocation(Location, boolean, boolean, boolean)
   */
  public static Location centerLocation(Location location) {
    return centerLocation(location, true, true, true);
  }

  /**
   * Get the normalized degree
   *
   * @param degree the degree
   *
   * @return the normalized degree
   */
  public static float normalizeDegree(float degree) {
    return (Math.round(degree / 90f) * 90f);
  }

  /**
   * Normalize the yaw and pitch of the location
   *
   * @param location the location
   *
   * @return the normalized location
   */
  public static Location normalizeYawPitch(Location location) {
    return new Location(
      location.getWorld(),
      location.getX(),
      location.getY(),
      location.getZ(),
      normalizeDegree(location.getYaw()),
      normalizeDegree(location.getPitch())
    );
  }
}
