package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {

  private static final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
  private static final Map<Plugin, Supplier<String>> pluginPrefixMap = new HashMap<>();
  private static Supplier<String> defaultPrefix = () -> "&7[&cHSCore&7] &f";

  private MessageUtils() {

  }

  /**
   * Convert to colored string. Support hex color with "&amp;#rrggbb"
   *
   * @param input the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorize(@NotNull final String input) {
    if (input.trim().isEmpty()) {
      return input;
    }

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
    return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
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
   * Get prefix of a plugin
   *
   * @param plugin the plugin
   *
   * @return the prefix
   */
  public static Optional<String> getPrefix(@NotNull final Plugin plugin) {
    return Optional.ofNullable(pluginPrefixMap.get(plugin)).map(Supplier::get);
  }

  /**
   * Set the prefix of a plugin
   *
   * @param plugin the plugin
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Plugin plugin, @NotNull final Supplier<String> prefix) {
    pluginPrefixMap.put(plugin, prefix);
  }

  /**
   * Set the prefix of a plugin
   *
   * @param plugin the plugin
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Plugin plugin, @NotNull final String prefix) {
    setPrefix(plugin, () -> prefix);
  }

  /**
   * Remove the prefix of a plugin
   *
   * @param plugin the plugin
   */
  public static void removePrefix(@NotNull final Plugin plugin) {
    pluginPrefixMap.remove(plugin);
  }
}
