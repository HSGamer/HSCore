package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Methods on messages on Bukkit
 */
public final class MessageUtils {
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
      receiver.sendMessage(ColorUtils.colorize(prefix + message));
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
}
