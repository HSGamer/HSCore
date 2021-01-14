package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {

  private static final Pattern hexPattern = Pattern.compile("(?<!\\\\)&#([A-Fa-f0-9]{6})");
  private static final Map<Object, Supplier<String>> objectPrefixMap = new HashMap<>();
  private static Supplier<String> defaultPrefix = () -> "&7[&cHSCore&7] &f";

  private MessageUtils() {

  }

  /**
   * Convert to colored string
   *
   * @param input the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorize(@NotNull final String input) {
    return colorizeHex(colorize('&', input));
  }

  /**
   * Convert HEX ("&amp;#rrggbb") string to color
   *
   * @param input the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorizeHex(@NotNull final String input) {
    Matcher matcher = hexPattern.matcher(input);
    StringBuffer buffer = new StringBuffer(input.length() + 4 * 8);
    while (matcher.find()) {
      String group = matcher.group(1);
      matcher.appendReplacement(buffer, COLOR_CHAR + "x"
        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
      );
    }
    return matcher.appendTail(buffer).toString();
  }

  /**
   * Convert to colored string
   *
   * @param altColorChar the alternative color char
   * @param input        the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorize(final char altColorChar, @NotNull final String input) {
    if (input.isEmpty()) {
      return input;
    }
    char[] chars = input.toCharArray();
    int i = 0;
    int oi = 0;
    while (i < chars.length - 1) {
      if (chars[i] == altColorChar && Character.isLetterOrDigit(chars[i + 1])) {
        chars[oi++] = COLOR_CHAR;
        chars[oi++] = Character.toLowerCase(chars[++i]);
      } else if (chars[i] == '\\' && chars[i + 1] == altColorChar) {
        chars[oi++] = chars[++i];
      } else {
        chars[oi++] = chars[i];
      }
      i++;
    }
    if (i == chars.length - 1) {
      chars[oi++] = chars[i];
    }
    return new String(chars, 0, Math.min(oi, chars.length));
  }

  /**
   * Send message
   *
   * @param sender  the receiver
   * @param message the message
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull final String message) {
    sendMessage(sender, message, defaultPrefix);
  }

  /**
   * Send message with prefix
   *
   * @param sender  the receiver
   * @param message the message
   * @param prefix  the prefix
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull final String message, @NotNull final String prefix) {
    sender.sendMessage(colorize(prefix + message));
  }

  /**
   * Send message with prefix
   *
   * @param sender  the receiver
   * @param message the message
   * @param prefix  the prefix
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull final String message, @NotNull final Supplier<String> prefix) {
    sendMessage(sender, message, prefix.get());
  }

  /**
   * Send message with the prefix from the plugin
   *
   * @param sender  the receiver
   * @param message the message
   * @param plugin  the plugin
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull final String message, @NotNull final Plugin plugin) {
    sendMessage(sender, message, getPrefix(plugin).orElse(getPrefix()));
  }

  /**
   * Send message
   *
   * @param uuid    the unique id of the receiver
   * @param message the message
   */
  public static void sendMessage(@NotNull final UUID uuid, @NotNull final String message) {
    sendMessage(uuid, message, defaultPrefix);
  }

  /**
   * Send message with prefix
   *
   * @param uuid    the unique id of the receiver
   * @param message the message
   * @param prefix  the prefix
   */
  public static void sendMessage(@NotNull final UUID uuid, @NotNull final String message, @NotNull final String prefix) {
    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> sendMessage(player, message, prefix));
  }

  /**
   * Send message with prefix
   *
   * @param uuid    the unique id of the receiver
   * @param message the message
   * @param prefix  the prefix
   */
  public static void sendMessage(@NotNull final UUID uuid, @NotNull final String message, @NotNull final Supplier<String> prefix) {
    sendMessage(uuid, message, prefix.get());
  }

  /**
   * Send message with the prefix from the plugin
   *
   * @param uuid    the unique id of the receiver
   * @param message the message
   * @param plugin  the plugin
   */
  public static void sendMessage(@NotNull final UUID uuid, @NotNull final String message, @NotNull final Plugin plugin) {
    sendMessage(uuid, message, getPrefix(plugin).orElse(getPrefix()));
  }

  /**
   * Get the default prefix
   *
   * @return the prefix
   */
  @NotNull
  public static String getPrefix() {
    return defaultPrefix.get();
  }

  /**
   * Set the default prefix
   *
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Supplier<String> prefix) {
    MessageUtils.defaultPrefix = prefix;
  }

  /**
   * Set the default prefix
   *
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final String prefix) {
    setPrefix(() -> prefix);
  }

  /**
   * Get prefix of an object
   *
   * @param object the object
   *
   * @return the prefix
   */
  public static Optional<String> getPrefix(@NotNull final Object object) {
    return Optional.ofNullable(objectPrefixMap.get(object)).map(Supplier::get);
  }

  /**
   * Set the prefix of an object
   *
   * @param object the object
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Object object, @NotNull final Supplier<String> prefix) {
    objectPrefixMap.put(object, prefix);
  }

  /**
   * Set the prefix of an object
   *
   * @param object the object
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Object object, @NotNull final String prefix) {
    setPrefix(object, () -> prefix);
  }

  /**
   * Remove the prefix of an object
   *
   * @param object the object
   */
  public static void removePrefix(@NotNull final Object object) {
    objectPrefixMap.remove(object);
  }
}
