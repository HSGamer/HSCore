package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The helper class for server versions
 */
public final class VersionUtils {
  private static final int VERSION;

  static {
    Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher(Bukkit.getVersion());
    if (matcher.find()) {
      VERSION = Integer.parseInt(matcher.group(1));
    } else {
      VERSION = -1;
    }
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
    return VERSION;
  }

  /**
   * Check if the server version is at least the given version
   *
   * @param version the version to check
   *
   * @return true if it is
   */
  public static boolean isAtLeast(int version) {
    return VERSION >= version;
  }
}
