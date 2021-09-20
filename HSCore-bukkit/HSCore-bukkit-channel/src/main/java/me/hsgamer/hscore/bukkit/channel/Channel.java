package me.hsgamer.hscore.bukkit.channel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

/**
 * The base channel
 */
public abstract class Channel implements PluginMessageListener {
  protected static final String MAIN_CHANNEL = "BungeeCord";
  private final String name;
  private final Plugin plugin;

  /**
   * Create a new channel
   *
   * @param name   the channel name
   * @param plugin the plugin
   */
  protected Channel(String name, Plugin plugin) {
    this.name = name;
    this.plugin = plugin;
  }

  /**
   * Register the channel
   */
  public void register() {
    this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, this.name, this);
    if (!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, this.name)) {
      this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, this.name);
    }
  }

  /**
   * Unregister the channel
   */
  public void unregister() {
    this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin, this.name, this);
    if (this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, this.name)) {
      this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, this.name);
    }
  }

  /**
   * Handle the received data from plugin messaging
   *
   * @param player the player involved
   * @param data   the data
   */
  public abstract void handleMessage(Player player, byte[] data);

  /**
   * Send the data via the channel
   *
   * @param data the data
   */
  public void send(byte[] data) {
    this.send(this.plugin.getServer(), data);
  }

  /**
   * Forward the data to other servers
   *
   * @param recipient the recipient
   * @param toServer  the server to forward
   * @param data      the data
   */
  public void sendForward(PluginMessageRecipient recipient, String toServer, byte[] data) {
    ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
    dataOutput.writeUTF("Forward");
    dataOutput.writeUTF(toServer);
    dataOutput.writeUTF(this.name);
    dataOutput.write(data);

    if (!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, MAIN_CHANNEL)) {
      this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, MAIN_CHANNEL);
    }
    recipient.sendPluginMessage(this.plugin, MAIN_CHANNEL, dataOutput.toByteArray());
  }

  /**
   * Forward the data to other servers
   *
   * @param toServer the server to forward
   * @param data     the data
   */
  public void sendForward(String toServer, byte[] data) {
    sendForward(this.plugin.getServer(), toServer, data);
  }

  /**
   * Send the data via the channel with the recipient
   *
   * @param recipient the recipient
   * @param data      the data
   */
  public void send(PluginMessageRecipient recipient, byte[] data) {
    recipient.sendPluginMessage(this.plugin, this.name, data);
  }

  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] data) {
    if (channel.equalsIgnoreCase(this.name)) {
      handleMessage(player, data);
    }
  }

  /**
   * Get the channel's name
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the plugin providing the channel
   *
   * @return the plugin
   */
  public Plugin getPlugin() {
    return plugin;
  }
}
