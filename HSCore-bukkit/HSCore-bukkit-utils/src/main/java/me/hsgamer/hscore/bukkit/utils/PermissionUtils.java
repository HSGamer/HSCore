package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   * @return the permission
   */
  @NotNull
  public static Permission createPermission(@NotNull final String name, @Nullable final String description,
                                            @Nullable final PermissionDefault permissionDefault) {
    Permission permission = new Permission(name);
    if (description != null) {
      permission.setDescription(description);
    }
    if (permissionDefault != null) {
      permission.setDefault(permissionDefault);
    }
    Bukkit.getServer().getPluginManager().addPermission(permission);
    return permission;
  }
}
