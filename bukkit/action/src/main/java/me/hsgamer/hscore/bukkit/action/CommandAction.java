package me.hsgamer.hscore.bukkit.action;

import io.github.projectunified.minelib.scheduler.entity.EntityScheduler;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * The action to execute a command
 */
public abstract class CommandAction implements Action {
  protected final Plugin plugin;
  private final String command;

  /**
   * Create a new action
   *
   * @param plugin  the plugin
   * @param command the command
   */
  protected CommandAction(Plugin plugin, String command) {
    this.plugin = plugin;
    this.command = command;
  }

  private static String normalize(String command) {
    return command.startsWith("/") ? command : "/" + command;
  }

  /**
   * Accept the command
   *
   * @param player  the player
   * @param command the command
   */
  protected abstract void accept(Player player, String command);

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    String replaced = stringReplacer.replaceOrOriginal(command, uuid);
    Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      process.next();
      return;
    }

    String command = normalize(replaced);
    EntityScheduler.get(plugin, player)
      .run(() -> {
        try {
          accept(player, command);
        } finally {
          process.next();
        }
      }, process::next);
  }
}
