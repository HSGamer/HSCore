package me.hsgamer.hscore.bungeecord.channel;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * The listener of {@link Channel}
 */
public final class ChannelListener implements Listener {
  private final Channel channel;

  ChannelListener(Channel channel) {
    this.channel = channel;
  }

  @EventHandler
  public void onReceive(PluginMessageEvent event) {
    if (channel.getName().equalsIgnoreCase(event.getTag())) {
      channel.handleMessage(event);
    }
  }
}
