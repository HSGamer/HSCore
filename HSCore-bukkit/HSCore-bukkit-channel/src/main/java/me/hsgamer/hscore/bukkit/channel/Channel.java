package me.hsgamer.hscore.bukkit.channel;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * The base channel
 */
public abstract class Channel implements PluginMessageListener {
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
    this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, this.name);
  }

  /**
   * Unregister the channel
   */
  public void unregister() {
    this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin, this.name, this);
    this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, this.name);
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
    this.plugin.getServer().sendPluginMessage(this.plugin, this.name, data);
  }

  /**
   * Send the data via the channel with the player as the source
   *
   * @param player the source player
   * @param data   the data
   */
  public void send(Player player, byte[] data) {
    player.sendPluginMessage(this.plugin, this.name, data);
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
