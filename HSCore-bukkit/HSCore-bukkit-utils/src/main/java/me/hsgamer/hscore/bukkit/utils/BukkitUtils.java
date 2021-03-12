package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Methods on Bukkit
 */
public final class BukkitUtils {
  private static final Map<String, CompletableFuture<OfflinePlayer>> offlinePlayersByNameMap = new ConcurrentHashMap<>();
  private static final Map<UUID, CompletableFuture<OfflinePlayer>> offlinePlayersByIdMap = new ConcurrentHashMap<>();

  private BukkitUtils() {
    // EMPTY
  }

  /**
   * Get the offline player asynchronously
   *
   * @param name the player's name
   *
   * @return the offline player
   */
  @SuppressWarnings("deprecation")
  @NotNull
  public static CompletableFuture<OfflinePlayer> getOfflinePlayerAsync(@NotNull String name) {
    // noinspection deprecation
    return offlinePlayersByNameMap.computeIfAbsent(name, s -> CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(name)));
  }

  /**
   * Get the offline player asynchronously
   *
   * @param uuid the player's unique id
   *
   * @return the offline player
   */
  @NotNull
  public static CompletableFuture<OfflinePlayer> getOfflinePlayerAsync(@NotNull UUID uuid) {
    return offlinePlayersByIdMap.computeIfAbsent(uuid, s -> CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(uuid)));
  }

  /**
   * Get ping
   *
   * @param player the player
   *
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
    return depends.parallelStream()
      .filter(depend -> Bukkit.getPluginManager().getPlugin(depend) == null)
      .collect(Collectors.toList());
  }

  /**
   * Check if the string is the username (https://github.com/CryptoMorin/XSeries/blob/3ec904a8d85695729ad51e7bbd06a65362357706/src/main/java/com/cryptomorin/xseries/SkullUtils.java#L200-L209)
   *
   * @param string the input string
   *
   * @return true if it is
   */
  public static boolean isUsername(@NotNull String string) {
    int len = string.length();
    if (len < 3 || len > 16) return false;

    for (char ch : string.toCharArray()) {
      if (ch != '_' && !(ch >= 'A' && ch <= 'Z') && !(ch >= 'a' && ch <= 'z') && !(ch >= '0' && ch <= '9'))
        return false;
    }
    return true;
  }
}
