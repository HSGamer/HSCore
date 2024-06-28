package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The helper class for server versions
 */
public final class VersionUtils {
  private static final int MAJOR_VERSION;
  private static final int MINOR_VERSION;
  private static final boolean IS_CRAFTBUKKIT_MAPPED;

  static {
    Matcher versionMatcher = Pattern.compile("MC: \\d\\.(\\d+)(\\.(\\d+))?").matcher(Bukkit.getVersion());
    if (versionMatcher.find()) {
      MAJOR_VERSION = Integer.parseInt(versionMatcher.group(1));
      MINOR_VERSION = Optional.ofNullable(versionMatcher.group(3)).filter(s -> !s.isEmpty()).map(Integer::parseInt).orElse(0);
    } else {
      MAJOR_VERSION = -1;
      MINOR_VERSION = -1;
    }

    Matcher packageMatcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
    IS_CRAFTBUKKIT_MAPPED = packageMatcher.find();
  }

  private VersionUtils() {
    // EMPTY
  }

  /**
   * Get the major version of the server
   *
   * @return the version
   */
  public static int getMajorVersion() {
    return MAJOR_VERSION;
  }

  /**
   * Get the minor version of the server
   *
   * @return the version
   */
  public static int getMinorVersion() {
    return MINOR_VERSION;
  }

  /**
   * Check if the server version is at least the given version
   *
   * @param version the version to check
   *
   * @return true if it is
   */
  public static boolean isAtLeast(int version) {
    return MAJOR_VERSION >= version;
  }

  /**
   * Check if the server version is at least the given version
   *
   * @param majorVersion the major version to check
   * @param minorVersion the minor version to check
   *
   * @return true if it is
   */
  public static boolean isAtLeast(int majorVersion, int minorVersion) {
    return MAJOR_VERSION > majorVersion || (MAJOR_VERSION == majorVersion && MINOR_VERSION >= minorVersion);
  }

  /**
   * Check if the server version is at the given version
   *
   * @param majorVersion the major version to check
   *
   * @return true if it is
   */
  public static boolean isAt(int majorVersion) {
    return MAJOR_VERSION == majorVersion;
  }

  /**
   * Check if the server version is at the given version
   *
   * @param majorVersion the major version to check
   * @param minorVersion the minor version to check
   *
   * @return true if it is
   */
  public static boolean isAt(int majorVersion, int minorVersion) {
    return MAJOR_VERSION == majorVersion && MINOR_VERSION == minorVersion;
  }

  /**
   * Check if the server version is newer than the given version
   *
   * @param majorVersion the major version to check
   *
   * @return true if it is
   */
  public static boolean isNewerThan(int majorVersion) {
    return MAJOR_VERSION > majorVersion;
  }

  /**
   * Check if the server version is newer than the given version
   *
   * @param majorVersion the major version to check
   * @param minorVersion the minor version to check
   *
   * @return true if it is
   */
  public static boolean isNewerThan(int majorVersion, int minorVersion) {
    return MAJOR_VERSION > majorVersion || (MAJOR_VERSION == majorVersion && MINOR_VERSION > minorVersion);
  }

  /**
   * Check if the server version is lower than the given version
   *
   * @param majorVersion the major version to check
   *
   * @return true if it is
   */
  public static boolean isLowerThan(int majorVersion) {
    return MAJOR_VERSION < majorVersion;
  }

  /**
   * Check if the server version is lower than the given version
   *
   * @param majorVersion the major version to check
   * @param minorVersion the minor version to check
   *
   * @return true if it is
   */
  public static boolean isLowerThan(int majorVersion, int minorVersion) {
    return MAJOR_VERSION < majorVersion || (MAJOR_VERSION == majorVersion && MINOR_VERSION < minorVersion);
  }

  /**
   * Check if the server is using CraftBukkit mappings.
   * CraftBukkit mappings are usually used in Spigot and old Paper versions.
   * It's useful to check whether the server is using CraftBukkit mappings (Spigot, old Paper) or new Paper mappings.
   * <a href="https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/">More info</a>
   *
   * @return true if it is
   */
  public static boolean isCraftBukkitMapped() {
    return IS_CRAFTBUKKIT_MAPPED;
  }
}
