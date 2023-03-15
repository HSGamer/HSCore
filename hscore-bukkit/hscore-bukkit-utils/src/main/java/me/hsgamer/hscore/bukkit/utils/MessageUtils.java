package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {
  private static final Map<Object, Supplier<String>> objectPrefixMap = new HashMap<>();
  private static Supplier<String> defaultPrefix = () -> "&7[&cHSCore&7] &f";

  private MessageUtils() {
    // EMPTY
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
      receiver.sendMessage(ColorUtils.colorize(receiver, prefix + message));
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
   * @param receivers the collection of receivers
   * @param message   the message
   */
  public static void sendMessage(@NotNull final Collection<CommandSender> receivers, @NotNull final String message) {
    receivers.forEach(player -> sendMessage(player, message));
  }

  /**
   * Send message with prefix
   *
   * @param receivers the collection of receivers
   * @param message   the message
   * @param prefix    the prefix
   */
  public static void sendMessage(@NotNull final Collection<CommandSender> receivers, @NotNull final String message, @NotNull final String prefix) {
    receivers.forEach(player -> sendMessage(player, message, prefix));
  }

  /**
   * Send message with prefix
   *
   * @param receivers the collection of receivers
   * @param message   the message
   * @param prefix    the prefix
   */
  public static void sendMessage(@NotNull final Collection<CommandSender> receivers, @NotNull final String message, @NotNull final Supplier<String> prefix) {
    receivers.forEach(player -> sendMessage(player, message, prefix));
  }

  /**
   * Send message with the prefix from the plugin
   *
   * @param receivers the collection of receivers
   * @param message   the message
   * @param object    the object
   */
  public static void sendMessage(@NotNull Collection<CommandSender> receivers, @NotNull final String message, @NotNull final Object object) {
    receivers.forEach(player -> sendMessage(player, message, object));
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
   * @param object  the object
   */
  public static void sendMessage(@NotNull final UUID uuid, @NotNull final String message, @NotNull final Object object) {
    sendMessage(uuid, message, getPrefix(object).orElse(getPrefix()));
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
