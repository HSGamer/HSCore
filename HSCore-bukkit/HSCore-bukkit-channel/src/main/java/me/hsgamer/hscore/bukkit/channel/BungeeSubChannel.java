package me.hsgamer.hscore.bukkit.channel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

/**
 * The sub-channel for the main BungeeCord channel
 */
public abstract class BungeeSubChannel extends Channel {
  private final String subChannel;

  /**
   * Create a new sub-channel
   *
   * @param subChannel the sub-channel name
   * @param plugin     the plugin
   */
  protected BungeeSubChannel(String subChannel, Plugin plugin) {
    super(MAIN_CHANNEL, plugin);
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
   * Handle the received data from plugin messaging through sub-channel
   *
   * @param player    the player involved
   * @param dataInput the data input
   */
  public abstract void handleSubChannelMessage(Player player, ByteArrayDataInput dataInput);

  @Override
  public void handleMessage(Player player, byte[] data) {
    ByteArrayDataInput input = ByteStreams.newDataInput(data);
    String channel = input.readUTF();
    if (this.subChannel.equals(channel)) {
      handleSubChannelMessage(player, input);
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
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(this.subChannel);
    out.write(data);
    super.send(recipient, out.toByteArray());
  }
}
