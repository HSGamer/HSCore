package me.hsgamer.hscore.bukkit.utils;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {

  private static final Map<Object, Supplier<String>> objectPrefixMap = new HashMap<>();
  private static final Map<Character, Supplier<Character>> colorMappers = Collections.singletonMap('u', () -> getRandomColor().getChar());
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
    return colorize('&', colorizeHex('&', input));
  }

  /**
   * Convert HEX string to color.
   * The HEX string format is "?#rrggbb", where "?" is the alternative color char
   *
   * @param altColorChar the alternative color char
   * @param input        the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorizeHex(final char altColorChar, @NotNull final String input) {
    return StringUtils.replaceHex(altColorChar, hex ->
        COLOR_CHAR + "x"
          + COLOR_CHAR + hex[0] + COLOR_CHAR + hex[1]
          + COLOR_CHAR + hex[2] + COLOR_CHAR + hex[3]
          + COLOR_CHAR + hex[4] + COLOR_CHAR + hex[5],
      input);
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
    return StringUtils.replaceChar(altColorChar, COLOR_CHAR, input, colorMappers);
  }

  /**
   * Get a random chat color
   *
   * @return the chat color
   */
  @NotNull
  public static ChatColor getRandomColor() {
    return Objects.requireNonNull(CollectionUtils.pickRandom(ChatColor.values(), ChatColor::isColor));
  }

  /**
   * Send message
   *
   * @param receiver the receiver
   * @param message  the message
   */
  public static void sendMessage(@NotNull final CommandSender receiver, @NotNull final String message) {
    sendMessage(receiver, message, defaultPrefix);
  }

  /**
   * Send message with prefix
   *
   * @param receiver the receiver
   * @param message  the message
   * @param prefix   the prefix
   */
  public static void sendMessage(@NotNull final CommandSender receiver, @NotNull final String message, @NotNull final String prefix) {
    if (!message.isEmpty()) {
      receiver.sendMessage(colorize(prefix + message));
    }
  }

  /**
   * Send message with prefix
   *
   * @param receiver the receiver
   * @param message  the message
   * @param prefix   the prefix
   */
  public static void sendMessage(@NotNull final CommandSender receiver, @NotNull final String message, @NotNull final Supplier<String> prefix) {
    sendMessage(receiver, message, prefix.get());
  }

  /**
   * Send message with the prefix from the object
   *
   * @param receiver the receiver
   * @param message  the message
   * @param object   the object
   */
  public static void sendMessage(@NotNull final CommandSender receiver, @NotNull final String message, @NotNull final Object object) {
    sendMessage(receiver, message, getPrefix(object).orElse(getPrefix()));
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
