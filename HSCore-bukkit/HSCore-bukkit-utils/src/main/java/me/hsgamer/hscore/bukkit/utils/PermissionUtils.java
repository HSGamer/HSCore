package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Methods on Permissions
 */
public class PermissionUtils {

  private PermissionUtils() {
    // EMPTY
  }

  /**
   * Create a permission
   *
   * @param name              the name
   * @param description       the description
   * @param permissionDefault who have this by default
   *
   * @return the permission
   */
  @NotNull
  public static Permission createPermission(@NotNull final String name, @Nullable final String description,
                                            @Nullable final PermissionDefault permissionDefault) {
    Permission permission = new Permission(name, description, permissionDefault);
    Bukkit.getServer().getPluginManager().addPermission(permission);
    return permission;
  }

  /**
   * Get the numbers from the permissions the permissible object has ([prefix].[number])
   *
   * @param permissible the permissible object
   * @param prefix      the permission prefix
   *
   * @return the stream of numbers
   */
  @NotNull
  public static Stream<Integer> getNumbersFromPermissions(Permissible permissible, String prefix) {
    return permissible.getEffectivePermissions().stream()
      .map(PermissionAttachmentInfo::getPermission)
      .filter(permission -> permission.startsWith(prefix))
      .map(permission -> permission.substring(prefix.length() + 1))
      .flatMap(rawNumber -> {
        try {
          return Stream.of(Integer.parseInt(rawNumber));
        } catch (Exception exception) {
          return Stream.empty();
        }
      });
  }
}
