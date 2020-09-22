package me.hsgamer.hscore.bukkit.utils;

import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
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
  }

  /**
   * Get ping
   *
   * @param player the player
   * @return the ping of the player
   */
  public static int getPing(@NotNull final Player player) {
    try {
      Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
      return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
    } catch (Exception e) {
      Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting ping", e);
      return -9;
    }
  }

  /**
   * Check if the server is Spigot
   *
   * @return whether the server is Spigot
   */
  public static boolean isSpigot() {
    return Validate.isClassLoaded("org.bukkit.entity.Player$Spigot");
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
   * @return the missing plugins
   */
  @NotNull
  public static List<String> getMissingDepends(@NotNull final List<String> depends) {
    return depends.stream()
      .filter(depend -> Bukkit.getPluginManager().getPlugin(depend) == null)
      .collect(Collectors.toList());
  }
}
