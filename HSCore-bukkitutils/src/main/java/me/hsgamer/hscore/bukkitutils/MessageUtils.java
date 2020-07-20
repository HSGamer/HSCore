package me.hsgamer.hscore.bukkitutils;

import java.util.function.Supplier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
  public static String colorize(String input) {
    if (input == null || input.trim().isEmpty()) {
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
  public static void sendMessage(CommandSender sender, String message) {
    sendMessage(sender, message, true);
  }

  /**
   * Send message with prefix
   *
   * @param sender    the receiver
   * @param message   the message
   * @param usePrefix whether the prefix should be included
   */
  public static void sendMessage(CommandSender sender, String message, boolean usePrefix) {
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
  public static String getPrefix() {
    return prefix.get();
  }

  /**
   * Set the prefix
   *
   * @param prefix the prefix
   */
  public static void setPrefix(Supplier<String> prefix) {
    MessageUtils.prefix = prefix;
  }
}
