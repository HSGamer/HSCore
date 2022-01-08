package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {

  private static final Pattern hexPattern = Pattern.compile("(?<!\\\\)(\\S)#([A-Fa-f0-9]{1,6})");
  private static final Map<Object, Supplier<String>> objectPrefixMap = new HashMap<>();
  private static Supplier<String> defaultPrefix = () -> "&7[&cHSCore&7] &f";

  private MessageUtils() {
    // EMPTY
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
    return colorizeHex('&', input);
  }

  /**
   * Convert HEX string to color.
   * The HEX string format is "?#rrggbb", which "?" is a single character indicates the start of the format
   *
   * @param altColorChar the alternative color char
   * @param input        the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorizeHex(final char altColorChar, @NotNull final String input) {
    Matcher matcher = hexPattern.matcher(input);
    StringBuffer buffer = new StringBuffer(input.length() + 4 * 8);
    while (matcher.find()) {
      String matchedChar = matcher.group(1);
      if (matchedChar.indexOf(altColorChar) < 0) {
        continue;
      }
      char[] hex = normalizeHexString(matcher.group(2));
      matcher.appendReplacement(buffer, COLOR_CHAR + "x"
        + COLOR_CHAR + hex[0] + COLOR_CHAR + hex[1]
        + COLOR_CHAR + hex[2] + COLOR_CHAR + hex[3]
        + COLOR_CHAR + hex[4] + COLOR_CHAR + hex[5]
      );
    }
    return matcher.appendTail(buffer).toString();
  }

  /**
   * Normalize the raw hex string to the 6-digit hex string
   *
   * @param input the raw hex string
   *
   * @return the 6-digit hex string
   */
  private static char[] normalizeHexString(@NotNull final String input) {
    char[] chars = new char[6];
    switch (input.length()) {
      case 1:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(0);
        chars[2] = input.charAt(0);
        chars[3] = input.charAt(0);
        chars[4] = input.charAt(0);
        chars[5] = input.charAt(0);
        break;
      case 2:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(0);
        chars[3] = input.charAt(1);
        chars[4] = input.charAt(0);
        chars[5] = input.charAt(1);
        break;
      case 3:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(0);
        chars[2] = input.charAt(1);
        chars[3] = input.charAt(1);
        chars[4] = input.charAt(2);
        chars[5] = input.charAt(2);
        break;
      case 4:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(2);
        chars[3] = input.charAt(3);
        chars[4] = '0';
        chars[5] = '0';
        break;
      case 5:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(2);
        chars[3] = input.charAt(3);
        chars[4] = '0';
        chars[5] = input.charAt(4);
        break;
      default:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(2);
        chars[3] = input.charAt(3);
        chars[4] = input.charAt(4);
        chars[5] = input.charAt(5);
        break;
    }
    return chars;
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
    int inputIndex = 0;
    int outputIndex = 0;
    while (inputIndex < chars.length - 1) {
      if (chars[inputIndex] == altColorChar && Character.isLetterOrDigit(chars[inputIndex + 1])) {
        chars[outputIndex++] = COLOR_CHAR;
        char currentChar = Character.toLowerCase(chars[++inputIndex]);
        chars[outputIndex++] = currentChar == 'u' ? getRandomColor().getChar() : currentChar;
        if (inputIndex + 1 < chars.length && Character.isWhitespace(chars[inputIndex + 1])) {
          inputIndex++;
        }
      } else if (chars[inputIndex] == '\\' && chars[inputIndex + 1] == altColorChar) {
        chars[outputIndex++] = chars[++inputIndex];
      } else {
        chars[outputIndex++] = chars[inputIndex];
      }
      inputIndex++;
    }
    if (inputIndex == chars.length - 1) {
      chars[outputIndex++] = chars[inputIndex];
    }
    return new String(chars, 0, Math.min(outputIndex, chars.length));
  }

  /**
   * Get a random chat color
   *
   * @return the chat color
   */
  public static ChatColor getRandomColor() {
    ChatColor[] values = ChatColor.values();
    ChatColor color;
    do {
      color = values[ThreadLocalRandom.current().nextInt(values.length - 1)];
    } while (color.equals(ChatColor.BOLD)
      || color.equals(ChatColor.ITALIC)
      || color.equals(ChatColor.STRIKETHROUGH)
      || color.equals(ChatColor.RESET)
      || color.equals(ChatColor.MAGIC)
      || color.equals(ChatColor.UNDERLINE));
    return color;
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
