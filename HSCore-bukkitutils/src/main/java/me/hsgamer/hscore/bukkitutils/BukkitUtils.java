package me.hsgamer.hscore.bukkitutils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class BukkitUtils {

  private BukkitUtils() {
  }

  /**
   * Get ping
   *
   * @param player the player
   * @return the ping of the player
   */
  public static String getPing(Player player) {
    try {
      Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
      return String.valueOf(entityPlayer.getClass().getField("ping").getInt(entityPlayer));
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
      Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting ping", e);
      return "ERROR GETTING PING";
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
  public static List<UUID> getAllUniqueIds() {
    return Arrays.stream(Bukkit.getOfflinePlayers())
        .parallel()
        .map(OfflinePlayer::getUniqueId)
        .collect(Collectors.toList());
  }

  /**
   * Get missing plugins from a list of given plugins
   *
   * @param depends the list of plugins
   * @return the missing plugins
   */
  public static List<String> getMissingDepends(List<String> depends) {
    List<String> list = new ArrayList<>();
    for (String depend : depends) {
      if (Bukkit.getPluginManager().getPlugin(depend) == null) {
        list.add(depend);
      }
    }
    return list;
  }
}
