package me.hsgamer.hscore.bukkit.channel;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The sub-channel for the main BungeeCord channel
 */
public abstract class BungeeSubChannel extends Channel {
  /**
   * The logger for ease
   */
  protected static final Logger LOGGER = Logger.getLogger(BungeeSubChannel.class.getSimpleName());

  private final String subChannel;

  /**
   * Create a new sub-channel
   *
   * @param subChannel the sub-channel name
   * @param plugin     the plugin
   */
  protected BungeeSubChannel(String subChannel, Plugin plugin) {
    super("BungeeCord", plugin);
    this.subChannel = subChannel;
  }

  /**
   * Get the sub-channel name
   *
   * @return the sub-channel name
   */
  public String getSubChannel() {
    return subChannel;
  }

  /**
   * Forward the data to other servers
   *
   * @param recipient the recipient
   * @param toServer  the server to forward
   * @param data      the data
   */
  public void sendForward(PluginMessageRecipient recipient, String toServer, byte[] data) {
    try (
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)
    ) {
      dataOutputStream.writeUTF("Forward");
      dataOutputStream.writeUTF(toServer);
      dataOutputStream.writeUTF(this.subChannel);
      dataOutputStream.writeShort(data.length);
      dataOutputStream.write(data);
      super.send(recipient, byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "There is an exception when sending data", e);
    }
  }

  /**
   * Forward the data to other servers
   *
   * @param toServer the server to forward
   * @param data     the data
   */
  public void sendForward(String toServer, byte[] data) {
    sendForward(getPlugin().getServer(), toServer, data);
  }

  /**
   * Handle the received data from plugin messaging through sub-channel
   *
   * @param player the player involved
   * @param data   the data
   */
  public abstract void handleSubChannelMessage(Player player, byte[] data);

  @Override
  public void handleMessage(Player player, byte[] data) {
    try (
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
      DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)
    ) {
      String channel = dataInputStream.readUTF();
      if (this.subChannel.equals(channel)) {
        int length = dataInputStream.available();
        byte[] subData = new byte[length];
        dataInputStream.readFully(subData);
        handleSubChannelMessage(player, subData);
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "There is an exception when reading data", e);
    }
  }

  /**
   * Send the data via the sub-channel with the recipient
   *
   * @param recipient the recipient
   * @param data      the data
   */
  @Override
  public void send(PluginMessageRecipient recipient, byte[] data) {
    try (
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)
    ) {
      dataOutputStream.writeUTF(this.subChannel);
      dataOutputStream.write(data);
      super.send(recipient, byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "There is an exception when sending data", e);
    }
  }
}
