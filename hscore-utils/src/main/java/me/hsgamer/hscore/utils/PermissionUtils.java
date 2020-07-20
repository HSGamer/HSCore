package me.hsgamer.hscore.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionUtils {

  private PermissionUtils() {

  }

  /**
   * Create a permission
   *
   * @param name              the name
   * @param description       the description
   * @param permissionDefault who have this by default
   * @return the permission
   */
  public static Permission createPermission(String name, String description,
      PermissionDefault permissionDefault) {
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
