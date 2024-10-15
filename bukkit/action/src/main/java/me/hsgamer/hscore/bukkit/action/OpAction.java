package me.hsgamer.hscore.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * The action to execute a command as op
 */
public class OpAction extends CommandAction {
  /**
   * Create a new action
   *
   * @param plugin  the plugin
   * @param command the command
   */
  public OpAction(Plugin plugin, String command) {
    super(plugin, command);
  }

  @Override
  protected void accept(Player player, String command) {
    if (player.isOp()) {
      player.chat(command);
    } else {
      try {
        player.setOp(true);
        player.chat(command);
      } finally {
        player.setOp(false);
      }
    }
  }
}
