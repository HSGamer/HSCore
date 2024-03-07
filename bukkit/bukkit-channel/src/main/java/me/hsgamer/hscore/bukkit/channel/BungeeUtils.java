package me.hsgamer.hscore.bukkit.channel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Methods for BungeeCord communication
 */
public final class BungeeUtils {
  private static final String BUNGEE_CHANNEL = "BungeeCord";
  private static Consumer<IOException> ioExceptionConsumer;

  private BungeeUtils() {
    // EMPTY
  }

  /**
   * Get the {@link IOException} consumer
   *
   * @return the consumer
   */
  public static Consumer<IOException> getIoExceptionConsumer() {
    if (ioExceptionConsumer == null) {
      Logger logger = Logger.getLogger(BungeeUtils.class.getSimpleName());
      ioExceptionConsumer = e -> logger.log(Level.SEVERE, "Failed to send message to BungeeCord", e);
    }
    return ioExceptionConsumer;
  }

  /**
   * Set the {@link IOException} consumer
   *
   * @param ioExceptionConsumer the consumer
   */
  public static void setIoExceptionConsumer(Consumer<IOException> ioExceptionConsumer) {
    BungeeUtils.ioExceptionConsumer = ioExceptionConsumer;
  }

  /**
   * Get the global plugin message recipient
   *
   * @return the global plugin message recipient
   */
  public static PluginMessageRecipient getGlobalRecipient() {
    return Bukkit.getServer();
  }

  /**
   * Build the data byte array
   *
   * @param dataOutputConsumer the consumer to build the data byte array
   *
   * @return the data byte array
   */
  public static byte[] getDataBytes(DataOutputConsumer dataOutputConsumer) {
    try (
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)
    ) {
      dataOutputConsumer.accept(dataOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      getIoExceptionConsumer().accept(e);
      return new byte[0];
    }
  }

  /**
   * Register the channel to the plugin
   *
   * @param plugin  the plugin
   * @param channel the channel
   */
  public static void register(Plugin plugin, String channel) {
    if (!plugin.getServer().getMessenger().isOutgoingChannelRegistered(plugin, channel)) {
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }
  }

  /**
   * Unregister the channel from the plugin
   *
   * @param plugin  the plugin
   * @param channel the channel
   */
  public static void unregister(Plugin plugin, String channel) {
    if (plugin.getServer().getMessenger().isOutgoingChannelRegistered(plugin, channel)) {
      plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
    }
  }

  /**
   * Register the BungeeCord main channel to the plugin
   *
   * @param plugin the plugin
   */
  public static void register(Plugin plugin) {
    register(plugin, BUNGEE_CHANNEL);
  }

  /**
   * Unregister the BungeeCord main channel from the plugin
   *
   * @param plugin the plugin
   */
  public static void unregister(Plugin plugin) {
    unregister(plugin, BUNGEE_CHANNEL);
  }

  /**
   * Send the data to the channel
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param channel   the channel
   * @param data      the data
   */
  public static void sendPluginMessage(Plugin plugin, PluginMessageRecipient recipient, String channel, byte[] data) {
    recipient.sendPluginMessage(plugin, channel, data);
  }

  /**
   * Send the data to the BungeeCord main channel
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param data      the data
   */
  public static void sendPluginMessage(Plugin plugin, PluginMessageRecipient recipient, byte[] data) {
    sendPluginMessage(plugin, recipient, BUNGEE_CHANNEL, data);
  }

  /**
   * Connect the recipient to the server
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param server    the server
   */
  public static void connectToServer(Plugin plugin, PluginMessageRecipient recipient, String server) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("Connect");
      dataStream.writeUTF(server);
    }));
  }

  /**
   * Connect the player to the server
   *
   * @param plugin     the plugin
   * @param recipient  the recipient
   * @param playerName the player name
   * @param server     the server
   */
  public static void connectOtherToServer(Plugin plugin, PluginMessageRecipient recipient, String playerName, String server) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("ConnectOther");
      dataStream.writeUTF(playerName);
      dataStream.writeUTF(server);
    }));
  }

  /**
   * Send the message to the player
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param player    the player
   * @param message   the message
   */
  public static void sendMessage(Plugin plugin, PluginMessageRecipient recipient, String player, String message) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("Message");
      dataStream.writeUTF(player);
      dataStream.writeUTF(message);
    }));
  }

  /**
   * Send the raw message to the player
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param player    the player
   * @param message   the message
   */
  public static void sendRawMessage(Plugin plugin, PluginMessageRecipient recipient, String player, String message) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("MessageRaw");
      dataStream.writeUTF(player);
      dataStream.writeUTF(message);
    }));
  }

  /**
   * Forward the data to the server
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param server    the server
   * @param channel   the channel
   * @param data      the data
   */
  public static void forward(Plugin plugin, PluginMessageRecipient recipient, String server, String channel, byte[] data) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("Forward");
      dataStream.writeUTF(server);
      dataStream.writeUTF(channel);
      dataStream.writeShort(data.length);
      dataStream.write(data);
    }));
  }

  /**
   * Forward the data to the player
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param player    the player
   * @param channel   the channel
   * @param data      the data
   */
  public static void forwardToPlayer(Plugin plugin, PluginMessageRecipient recipient, String player, String channel, byte[] data) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("ForwardToPlayer");
      dataStream.writeUTF(player);
      dataStream.writeUTF(channel);
      dataStream.writeShort(data.length);
      dataStream.write(data);
    }));
  }

  /**
   * Kick the player
   *
   * @param plugin    the plugin
   * @param recipient the recipient
   * @param player    the player
   * @param message   the message
   */
  public static void kickPlayer(Plugin plugin, PluginMessageRecipient recipient, String player, String message) {
    sendPluginMessage(plugin, recipient, getDataBytes(dataStream -> {
      dataStream.writeUTF("KickPlayer");
      dataStream.writeUTF(player);
      dataStream.writeUTF(message);
    }));
  }

  /**
   * The data output consumer
   */
  public interface DataOutputConsumer {
    /**
     * Accept the data output stream
     *
     * @param dataOutputStream the data output stream
     *
     * @throws IOException if an I/O error occurs
     */
    void accept(DataOutputStream dataOutputStream) throws IOException;
  }
}
