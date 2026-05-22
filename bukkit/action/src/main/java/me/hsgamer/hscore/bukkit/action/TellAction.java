package me.hsgamer.hscore.bukkit.action;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.UnaryOperator;

/**
 * The action to tell a message to the player
 */
public class TellAction implements Action {
  private final String message;
  private final UnaryOperator<String> colorizer;

  /**
   * Create a new action
   *
   * @param message   the message
   * @param colorizer a function to colorize the message
   */
  public TellAction(String message, UnaryOperator<String> colorizer) {
    this.message = message;
    this.colorizer = colorizer;
  }

  /**
   * Create a new action
   *
   * @param message the message
   */
  public TellAction(String message) {
    this(message, s -> ChatColor.translateAlternateColorCodes('&', s));
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      String replaced = stringReplacer.replaceOrOriginal(message, uuid);
      replaced = colorizer.apply(replaced);
      player.sendMessage(replaced);
    }
    process.next();
  }
}
