package me.hsgamer.hscore.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * The action to execute a command as the player
 */
public class PlayerAction extends CommandAction {
  /**
   * Create a new action
   *
   * @param plugin  the plugin
   * @param command the command
   */
  public PlayerAction(Plugin plugin, String command) {
    super(plugin, command);
  }

  @Override
  protected void accept(Player player, String command) {
    player.chat(command);
  }
}
