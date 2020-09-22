package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {

  private static Supplier<String> prefix = () -> "";

  private MessageUtils() {

  }

  /**
   * Convert to colored string
   *
   * @param input the string
   * @return the colored string
   */
  @NotNull
  public static String colorize(@NotNull final String input) {
    if (input.trim().isEmpty()) {
      return input;
    }
    return ChatColor.translateAlternateColorCodes('&', input);
  }

  /**
   * Send message
   *
   * @param sender  the receiver
   * @param message the message
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull final String message) {
    sendMessage(sender, message, true);
  }

  /**
   * Send message with prefix
   *
   * @param sender    the receiver
   * @param message   the message
   * @param usePrefix whether the prefix should be included
   */
  public static void sendMessage(@NotNull final CommandSender sender, @NotNull String message, final boolean usePrefix) {
    if (usePrefix) {
      message = prefix.get() + message;
    }
    sender.sendMessage(colorize(message));
  }

  /**
   * Get the prefix
   *
   * @return the prefix
   */
  @NotNull
  public static String getPrefix() {
    return prefix.get();
  }

  /**
   * Set the prefix
   *
   * @param prefix the prefix
   */
  public static void setPrefix(@NotNull final Supplier<String> prefix) {
    MessageUtils.prefix = prefix;
  }
}
