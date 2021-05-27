package me.hsgamer.hscore.bungeecord.channel;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * The base channel
 */
public abstract class Channel {
  private final String name;
  private final Plugin plugin;
  private final Listener listener = new Listener() {
    @EventHandler
    public void onReceive(PluginMessageEvent event) {
      if (getName().equalsIgnoreCase(event.getTag())) {
        handleMessage(event);
      }
    }
  };

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
    this.plugin.getProxy().registerChannel(this.name);
    this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this.listener);
  }

  /**
   * Unregister the channel
   */
  public void unregister() {
    this.plugin.getProxy().unregisterChannel(this.name);
    this.plugin.getProxy().getPluginManager().unregisterListener(this.listener);
  }

  /**
   * Handle the received data from plugin messaging
   *
   * @param event the plugin message event
   */
  public abstract void handleMessage(PluginMessageEvent event);

  /**
   * Send the data to all servers
   *
   * @param data the data
   */
  public void sendAll(byte[] data) {
    this.plugin.getProxy().getServers().values().forEach(serverInfo -> send(serverInfo, data));
  }

  /**
   * Send the data to a server
   *
   * @param server the server
   * @param data   the data
   */
  public void send(ServerInfo server, byte[] data) {
    server.sendData(this.name, data);
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
