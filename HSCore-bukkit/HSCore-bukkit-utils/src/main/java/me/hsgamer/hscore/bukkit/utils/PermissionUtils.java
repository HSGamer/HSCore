package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Methods on Permissions
 */
public class PermissionUtils {

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
}
