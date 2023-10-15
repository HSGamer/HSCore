package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Methods on Permissions
 */
public final class PermissionUtils {

  private PermissionUtils() {
    // EMPTY
  }

  /**
   * Get the strings from the permissions the permissible object has ([prefix].[string])
   *
   * @param permissible the permissible object
   * @param prefix      the permission prefix
   *
   * @return the stream of strings
   */
  @NotNull
  public static Stream<String> getStringsFromPermissions(Permissible permissible, String prefix) {
    return permissible.getEffectivePermissions().parallelStream()
      .map(PermissionAttachmentInfo::getPermission)
      .filter(permission -> permission.startsWith(prefix))
      .map(permission -> permission.substring(prefix.length() + 1));
  }

  /**
   * Get the numbers from the permissions the permissible object has ([prefix].[value])
   *
   * @param permissible     the permissible object
   * @param prefix          the permission prefix
   * @param numberConverter the number converter
   *
   * @return the stream of numbers
   *
   * @see #getStringsFromPermissions(Permissible, String)
   */
  @NotNull
  public static Stream<Number> getNumbersFromPermissions(Permissible permissible, String prefix, Function<String, Stream<Number>> numberConverter) {
    return getStringsFromPermissions(permissible, prefix).flatMap(numberConverter);
  }

  /**
   * Get the numbers from the permissions the permissible object has ([prefix].[value])
   *
   * @param permissible the permissible object
   * @param prefix      the permission prefix
   *
   * @return the stream of numbers
   *
   * @see #getNumbersFromPermissions(Permissible, String, Function)
   */
  @NotNull
  public static Stream<Number> getNumbersFromPermissions(Permissible permissible, String prefix) {
    return getNumbersFromPermissions(permissible, prefix, rawValue -> {
      try {
        return Stream.of(Integer.parseInt(rawValue));
      } catch (Exception e) {
        return Stream.empty();
      }
    });
  }

  /**
   * Check if the player has the permission
   *
   * @param player     the player
   * @param permission the permission. If it starts with "-", it will check if the player doesn't have the permission
   *
   * @return true if the player does
   */
  public static boolean hasPermission(Player player, String permission) {
    if (permission.startsWith("-")) {
      return !player.hasPermission(permission.substring(1).trim());
    } else {
      return player.hasPermission(permission);
    }
  }

  /**
   * Check if the player has one of the permissions
   *
   * @param player      the player
   * @param permissions the permissions
   *
   * @return true if the player does
   */
  public static boolean hasAnyPermission(Player player, Collection<Permission> permissions) {
    for (Permission permission : permissions) {
      if (player.hasPermission(permission)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the player has one of the permissions
   *
   * @param player      the player
   * @param permissions the permissions
   *
   * @return true if the player does
   */
  public static boolean hasAnyPermissionString(Player player, Collection<String> permissions) {
    for (String permission : permissions) {
      if (player.hasPermission(permission)) {
        return true;
      }
    }
    return false;
  }
}
