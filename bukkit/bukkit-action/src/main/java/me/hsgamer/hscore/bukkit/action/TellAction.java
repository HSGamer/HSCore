package me.hsgamer.hscore.bukkit.action;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The action to tell a message to the player
 */
public class TellAction implements Action {
  private final String message;

  /**
   * Create a new action
   *
   * @param message the message
   */
  public TellAction(String message) {
    this.message = message;
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      player.sendMessage(stringReplacer.replaceOrOriginal(message, uuid));
    }
    process.next();
  }
}
