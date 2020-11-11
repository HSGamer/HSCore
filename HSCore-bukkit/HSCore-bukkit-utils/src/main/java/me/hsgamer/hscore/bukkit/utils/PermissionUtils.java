package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

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
   * @param asyncAdd          should the permission be added asynchronously?
   *
   * @return the permission
   */
  @NotNull
  public static Permission createPermission(@NotNull final String name, @Nullable final String description,
                                            @Nullable final PermissionDefault permissionDefault, final boolean asyncAdd) {
    Permission permission = new Permission(name);
    if (description != null) {
      permission.setDescription(description);
    }
    if (permissionDefault != null) {
      permission.setDefault(permissionDefault);
    }
    addPermission(permission, asyncAdd);
    return permission;
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
    return createPermission(name, description, permissionDefault, true);
  }

  /**
   * Add a permission
   *
   * @param permission the permission
   * @param async      should the permission be added asynchronously?
   *
   * @return the completable future
   */
  @NotNull
  public static CompletableFuture<Void> addPermission(@NotNull final Permission permission, final boolean async) {
    final Runnable runnable = () -> Bukkit.getServer().getPluginManager().addPermission(permission);
    if (async) {
      return CompletableFuture.runAsync(runnable);
    } else {
      runnable.run();
      return CompletableFuture.completedFuture(null);
    }
  }

  /**
   * Add a permission
   *
   * @param permission the permission
   *
   * @return the completable future
   */
  @NotNull
  public static CompletableFuture<Void> addPermission(@NotNull final Permission permission) {
    return addPermission(permission, true);
  }
}
